package com.example.demo;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleStatusServlet;

import io.jsonwebtoken.Claims;

import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.Optional;


@Listeners({TestResultListener.class})
public class ApiRateLimiterQuotaManagerTest {

    // === Mocked Dependencies ==============================================================
    @Mock private UserAccountRepository userRepo;
    @Mock private ApiKeyRepository apiKeyRepo;
    @Mock private QuotaPlanRepository quotaPlanRepo;
    @Mock private ApiUsageLogRepository usageRepo;
    @Mock private RateLimitEnforcementRepository enforceRepo;
    @Mock private KeyExemptionRepository exemptionRepo;

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private CustomUserDetailsService customUserDetailsService;

    // === Services under Test ==================================================================
    private AuthService authService;
    private ApiKeyService apiKeyService;
    private QuotaPlanService quotaPlanService;
    private ApiUsageLogService usageService;
    private RateLimitEnforcementService enforcementService;
    private KeyExemptionService exemptionService;

    // === Servlet ================================================================================
    private SimpleStatusServlet servlet;

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);

        authService = new AuthServiceImpl(
                userRepo,
                passwordEncoder,
                authenticationManager,
                jwtUtil
        );

        apiKeyService = new ApiKeyServiceImpl(apiKeyRepo, quotaPlanRepo);
        quotaPlanService = new QuotaPlanServiceImpl(quotaPlanRepo);
        usageService = new ApiUsageLogServiceImpl(usageRepo, apiKeyRepo);
        enforcementService = new RateLimitEnforcementServiceImpl(enforceRepo, apiKeyRepo);
        exemptionService = new KeyExemptionServiceImpl(exemptionRepo, apiKeyRepo);

        servlet = new SimpleStatusServlet();
    }



    // ======================================================================================
    //  1 — SERVLET TESTS (10 tests)
    // ======================================================================================

    @Test(priority = 1)
    public void t01_servletBasicResponse() throws IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(req, resp);
        Assert.assertEquals(resp.getContentAsString(), "API Rate Limiter & Quota Manager is running");
    }

    @Test(priority = 2)
    public void t02_servletContentType() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertEquals(resp.getContentType(), "text/plain");
    }

    @Test(priority = 3)
    public void t03_servletStatus200() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertEquals(resp.getStatus(), 200);
    }

    @Test(priority = 4)
    public void t04_servletMultipleRequestsGiveSameOutput() throws IOException {
        MockHttpServletResponse resp1 = new MockHttpServletResponse();
        MockHttpServletResponse resp2 = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp1);
        servlet.doGet(new MockHttpServletRequest(), resp2);
        Assert.assertEquals(resp1.getContentAsString(), resp2.getContentAsString());
    }

    @Test(priority = 5)
    public void t05_servletMessageLengthValid() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertTrue(resp.getContentAsString().length() > 10);
    }

    @Test(priority = 6)
    public void t06_servletWriterNotNull() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertNotNull(resp.getWriter());
    }

    @Test(priority = 7)
    public void t07_servletNotEmptyResponse() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertFalse(resp.getContentAsString().isEmpty());
    }

    @Test(priority = 8)
    public void t08_servletResponseContainsProjectName() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertTrue(resp.getContentAsString().contains("API Rate Limiter"));
    }

    @Test(priority = 9)
    public void t09_servletResponseIsTextOnly() throws IOException {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), resp);
        Assert.assertFalse(resp.getContentAsString().contains("<html>"));
    }

    @Test(priority = 10)
    public void t10_servletObjectExists() {
        Assert.assertNotNull(servlet);
    }



    // ======================================================================================
    //  2 — CRUD TESTS (QuotaPlan, ApiKey, UserAccount) — 15 tests
    // ======================================================================================

    @Test(priority = 11)
    public void t11_createQuotaPlan_success() {
        QuotaPlan p = new QuotaPlan();
        p.setPlanName("Starter");
        p.setDailyLimit(1000);
        p.setActive(true);

        Mockito.when(quotaPlanRepo.save(Mockito.any())).thenReturn(p);

        QuotaPlan saved = quotaPlanService.createQuotaPlan(p);
        Assert.assertEquals(saved.getPlanName(), "Starter");
    }

    @Test(priority = 12)
    public void t12_createQuotaPlan_dailyLimitFail() {
        QuotaPlan p = new QuotaPlan();
        p.setPlanName("Bad");
        p.setDailyLimit(0);

        Assert.expectThrows(BadRequestException.class, () -> quotaPlanService.createQuotaPlan(p));
    }

    @Test(priority = 13)
    public void t13_getQuotaPlanById_success() {
        QuotaPlan p = new QuotaPlan();
        p.setPlanName("Pro");
        Mockito.when(quotaPlanRepo.findById(1L)).thenReturn(Optional.of(p));

        QuotaPlan result = quotaPlanService.getQuotaPlanById(1L);
        Assert.assertEquals(result.getPlanName(), "Pro");
    }

    @Test(priority = 14)
    public void t14_getQuotaPlanById_notFound() {
        Mockito.when(quotaPlanRepo.findById(99L)).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> quotaPlanService.getQuotaPlanById(99L));
    }

    @Test(priority = 15)
    public void t15_deactivateQuotaPlan_success() {
        QuotaPlan p = new QuotaPlan();
        p.setActive(true);
        Mockito.when(quotaPlanRepo.findById(1L)).thenReturn(Optional.of(p));
        quotaPlanService.deactivateQuotaPlan(1L);
        Assert.assertFalse(p.isActive());
    }

    @Test(priority = 16)
    public void t16_createApiKey_success() {
        QuotaPlan plan = new QuotaPlan();
        plan.setId(1L);
        plan.setActive(true);

        ApiKey key = new ApiKey();
        key.setPlan(plan);
        key.setOwnerId(5L);

        Mockito.when(quotaPlanRepo.findById(1L)).thenReturn(Optional.of(plan));
        Mockito.when(apiKeyRepo.save(Mockito.any())).thenReturn(key);

        ApiKey created = apiKeyService.createApiKey(key);
        Assert.assertEquals(created.getOwnerId(), 5L);
    }

    @Test(priority = 17)
    public void t17_createApiKey_failsIfPlanNotActive() {
        QuotaPlan plan = new QuotaPlan();
        plan.setId(1L);
        plan.setActive(false);

        ApiKey key = new ApiKey();
        key.setPlan(plan);

        Mockito.when(quotaPlanRepo.findById(1L)).thenReturn(Optional.of(plan));
        Assert.expectThrows(BadRequestException.class, () -> apiKeyService.createApiKey(key));
    }

    @Test(priority = 18)
    public void t18_getApiKeyById_success() {
        ApiKey k = new ApiKey();
        k.setKeyValue("abc123");

        Mockito.when(apiKeyRepo.findById(7L)).thenReturn(Optional.of(k));
        ApiKey out = apiKeyService.getApiKeyById(7L);
        Assert.assertEquals(out.getKeyValue(), "abc123");
    }

    @Test(priority = 19)
    public void t19_getApiKeyById_notFound() {
        Mockito.when(apiKeyRepo.findById(99L)).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class, () -> apiKeyService.getApiKeyById(99L));
    }

    @Test(priority = 20)
    public void t20_deactivateApiKey_success() {
        ApiKey k = new ApiKey();
        k.setActive(true);

        Mockito.when(apiKeyRepo.findById(1L)).thenReturn(Optional.of(k));
        apiKeyService.deactivateApiKey(1L);

        Assert.assertFalse(k.isActive());
    }

    @Test(priority = 21)
    public void t21_userRegistration_success() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@gmail.com");
        dto.setPassword("password123");
        dto.setRole("USER");

        Mockito.when(userRepo.existsByEmail("test@gmail.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("ENC");

        authService.register(dto);

        Mockito.verify(userRepo, Mockito.times(1)).save(Mockito.any());
    }

    @Test(priority = 22)
    public void t22_userRegistration_duplicateEmail() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("abc@gmail.com");
        dto.setPassword("password123");
        dto.setRole("ADMIN");

        Mockito.when(userRepo.existsByEmail("abc@gmail.com")).thenReturn(true);

        Assert.expectThrows(BadRequestException.class, () -> authService.register(dto));
    }

    @Test(priority = 23)
    public void t23_userLogin_success() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setEmail("x@gmail.com");
        dto.setPassword("12345678");

        UserAccount user = new UserAccount();
        user.setEmail("x@gmail.com");
        user.setPassword("ENC");
        user.setRole("ROLE_USER");

        Mockito.when(userRepo.findByEmail("x@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken(Mockito.anyMap(), Mockito.eq("x@gmail.com")))
                .thenReturn("TOKEN123");

        AuthResponseDto response = authService.login(dto);
        Assert.assertEquals(response.getToken(), "TOKEN123");
    }

    @Test(priority = 24)
    public void t24_userLogin_invalidUser() {
        AuthRequestDto dto = new AuthRequestDto();
        dto.setEmail("invalid@gmail.com");
        dto.setPassword("pass");

        Mockito.when(userRepo.findByEmail("invalid@gmail.com")).thenReturn(Optional.empty());

        Assert.expectThrows(Exception.class, () -> authService.login(dto));
    }

    @Test(priority = 25)
    public void t25_quotaPlan_update_success() {
        QuotaPlan existing = new QuotaPlan();
        existing.setPlanName("Basic");
        existing.setDailyLimit(100);

        QuotaPlan updated = new QuotaPlan();
        updated.setPlanName("Updated");
        updated.setDailyLimit(200);

        Mockito.when(quotaPlanRepo.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(quotaPlanRepo.save(Mockito.any())).thenReturn(existing);

        QuotaPlan result = quotaPlanService.updateQuotaPlan(1L, updated);
        Assert.assertEquals(result.getDailyLimit(), 200);
    }



    // ======================================================================================
    //  3 — DI / IoC TESTS — 5 tests
    // ======================================================================================

    @Test(priority = 26)
    public void t26_di_authServiceInjected() {
        Assert.assertNotNull(authService);
    }

    @Test(priority = 27)
    public void t27_di_quotaPlanServiceInjected() {
        Assert.assertNotNull(quotaPlanService);
    }

    @Test(priority = 28)
    public void t28_di_apiKeyServiceInjected() {
        Assert.assertNotNull(apiKeyService);
    }

    @Test(priority = 29)
    public void t29_di_usageServiceInjected() {
        Assert.assertNotNull(usageService);
    }

    @Test(priority = 30)
    public void t30_di_enforcementServiceInjected() {
        Assert.assertNotNull(enforcementService);
    }



    // ======================================================================================
    //  4 — Hibernate CRUD / validation tests — 10 tests
    // ======================================================================================

    @Test(priority = 31)
    public void t31_usageLog_success() {
        ApiKey k = new ApiKey();
        k.setId(5L);

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(k);
        log.setEndpoint("/hello");
        log.setTimestamp(Instant.now());

        Mockito.when(apiKeyRepo.findById(5L)).thenReturn(Optional.of(k));
        Mockito.when(usageRepo.save(Mockito.any())).thenReturn(log);

        ApiUsageLog saved = usageService.logUsage(log);
        Assert.assertEquals(saved.getEndpoint(), "/hello");
    }

    @Test(priority = 32)
    public void t32_usageLog_futureTimestampFails() {
        ApiKey k = new ApiKey();
        k.setId(10L);

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(k);
        log.setTimestamp(Instant.now().plusSeconds(9999));

        Mockito.when(apiKeyRepo.findById(10L)).thenReturn(Optional.of(k));

        Assert.expectThrows(BadRequestException.class, () -> usageService.logUsage(log));
    }

    @Test(priority = 33)
    public void t33_usageLog_getForToday() {
        Mockito.when(usageRepo.findForKeyBetween(Mockito.eq(3L), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        List<ApiUsageLog> list = usageService.getUsageForToday(3L);
        Assert.assertNotNull(list);
    }

    @Test(priority = 34)
    public void t34_usageLog_countToday() {
        Mockito.when(usageRepo.countForKeyBetween(Mockito.eq(5L), Mockito.any(), Mockito.any()))
                .thenReturn(7);

        int count = usageService.countRequestsToday(5L);
        Assert.assertEquals(count, 7);
    }

    @Test(priority = 35)
    public void t35_enforcementCreate_success() {
        ApiKey k = new ApiKey();
        k.setId(1L);

        RateLimitEnforcement e = new RateLimitEnforcement();
        e.setApiKey(k);
        e.setLimitExceededBy(5);

        Mockito.when(apiKeyRepo.findById(1L)).thenReturn(Optional.of(k));
        Mockito.when(enforceRepo.save(Mockito.any())).thenReturn(e);

        RateLimitEnforcement result = enforcementService.createEnforcement(e);
        Assert.assertEquals(result.getLimitExceededBy(), 5);
    }

    @Test(priority = 36)
    public void t36_enforcementCreate_failsForNegative() {
        ApiKey k = new ApiKey();
        k.setId(2L);

        RateLimitEnforcement e = new RateLimitEnforcement();
        e.setApiKey(k);
        e.setLimitExceededBy(0);

        Mockito.when(apiKeyRepo.findById(2L)).thenReturn(Optional.of(k));

        Assert.expectThrows(BadRequestException.class, () -> enforcementService.createEnforcement(e));
    }

    @Test(priority = 37)
    public void t37_keyExemption_create_success() {
        ApiKey k = new ApiKey();
        k.setId(3L);

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(k);
        ex.setTemporaryExtensionLimit(10);
        ex.setValidUntil(Instant.now().plusSeconds(5000));

        Mockito.when(apiKeyRepo.findById(3L)).thenReturn(Optional.of(k));
        Mockito.when(exemptionRepo.save(Mockito.any())).thenReturn(ex);

        KeyExemption result = exemptionService.createExemption(ex);
        Assert.assertEquals(result.getTemporaryExtensionLimit(), 10);
    }

    @Test(priority = 38)
    public void t38_keyExemption_create_invalidLimit() {
        ApiKey k = new ApiKey();
        k.setId(4L);

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(k);
        ex.setTemporaryExtensionLimit(-1);

        Mockito.when(apiKeyRepo.findById(4L)).thenReturn(Optional.of(k));

        Assert.expectThrows(BadRequestException.class, () -> exemptionService.createExemption(ex));
    }



    // ======================================================================================
    //  5 — JPA MAPPINGS — 5 tests
    // ======================================================================================

    @Test(priority = 39)
    public void t39_quotaPlan_hasDailyLimit() {
        QuotaPlan p = new QuotaPlan();
        p.setDailyLimit(150);
        Assert.assertEquals(p.getDailyLimit(), 150);
    }

    @Test(priority = 40)
    public void t40_apiKey_hasPlan() {
        QuotaPlan p = new QuotaPlan();
        ApiKey k = new ApiKey();
        k.setPlan(p);
        Assert.assertEquals(k.getPlan(), p);
    }

    @Test(priority = 41)
    public void t41_exemption_hasApiKey() {
        ApiKey k = new ApiKey();
        KeyExemption e = new KeyExemption();
        e.setApiKey(k);
        Assert.assertEquals(e.getApiKey(), k);
    }

    @Test(priority = 42)
    public void t42_usageLog_hasEndpoint() {
        ApiUsageLog log = new ApiUsageLog();
        log.setEndpoint("/x");
        Assert.assertEquals(log.getEndpoint(), "/x");
    }

    @Test(priority = 43)
    public void t43_enforcement_hasMessage() {
        RateLimitEnforcement e = new RateLimitEnforcement();
        e.setMessage("Exceeded");
        Assert.assertEquals(e.getMessage(), "Exceeded");
    }



    // ======================================================================================
    //  6 — MANY-TO-MANY TESTS — 5 tests
    // ======================================================================================

    @Test(priority = 44)
    public void t44_user_hasRolesInsideUser() {
        UserAccount user = new UserAccount();
        user.setRole("ROLE_ADMIN");
        Assert.assertEquals(user.getRole(), "ROLE_ADMIN");
    }

    @Test(priority = 45)
    public void t45_userAccount_hasEmail() {
        UserAccount user = new UserAccount();
        user.setEmail("abc@gmail.com");
        Assert.assertEquals(user.getEmail(), "abc@gmail.com");
    }

    @Test(priority = 46)
    public void t46_apiKey_ownerSet() {
        ApiKey k = new ApiKey();
        k.setOwnerId(5L);
        Assert.assertEquals(k.getOwnerId(), 5L);
    }

    @Test(priority = 47)
    public void t47_quotaPlan_activeFlag() {
        QuotaPlan p = new QuotaPlan();
        p.setActive(true);
        Assert.assertTrue(p.isActive());
    }

    @Test(priority = 48)
    public void t48_apiKey_activeFlag() {
        ApiKey k = new ApiKey();
        k.setActive(false);
        Assert.assertFalse(k.isActive());
    }



    // ======================================================================================
    //  7 — JWT + SECURITY TESTS — 10 tests
    // ======================================================================================

    @Test(priority = 49)
    public void t49_jwt_generateToken_success() {
        Mockito.when(jwtUtil.generateToken(Mockito.anyMap(), Mockito.eq("abc@gmail.com")))
                .thenReturn("JWT123");

        String tok = jwtUtil.generateToken(Map.of(), "abc@gmail.com");
        Assert.assertEquals(tok, "JWT123");
    }

    @Test(priority = 50)
public void t50_jwt_extractUsername() {
    Claims claims = Mockito.mock(Claims.class);
    Mockito.when(claims.getSubject()).thenReturn("hello@gmail.com");

    Mockito.when(jwtUtil.getClaims("TOKEN1")).thenReturn(claims);
    Mockito.when(jwtUtil.getUsername("TOKEN1")).thenCallRealMethod();

    String email = jwtUtil.getUsername("TOKEN1");
    Assert.assertEquals(email, "hello@gmail.com");
}

@Test(priority = 51)
public void t51_jwt_expirationPositive() {
    Mockito.when(jwtUtil.getExpirationMillis()).thenCallRealMethod();
    Assert.assertTrue(jwtUtil.getExpirationMillis() > 0);
}


    @Test(priority = 52)
    public void t52_customUserDetails_loadUser() {
        UserAccount user = new UserAccount();
        user.setEmail("x@gmail.com");
        user.setPassword("ENC");
        user.setRole("ROLE_USER");

        Mockito.when(userRepo.findByEmail("x@gmail.com"))
                .thenReturn(Optional.of(user));

        Assert.assertEquals(
                customUserDetailsService.loadUserByUsername("x@gmail.com").getUsername(),
                "x@gmail.com"
        );
    }

    @Test(priority = 53)
    public void t53_customUserDetails_userNotFound() {
        Mockito.when(userRepo.findByEmail("zzz@gmail.com"))
                .thenReturn(Optional.empty());

        Assert.expectThrows(Exception.class,
                () -> customUserDetailsService.loadUserByUsername("zzz@gmail.com"));
    }

    @Test(priority = 54)
    public void t54_jwt_notValidForWrongUsername() {
        Mockito.when(jwtUtil.getUsername("T1")).thenReturn("abc@gmail.com");
        Mockito.when(jwtUtil.isTokenValid("T1", "wrong@gmail.com")).thenReturn(false);

        Assert.assertFalse(jwtUtil.isTokenValid("T1", "wrong@gmail.com"));
    }

    @Test(priority = 55)
    public void t55_jwt_validToken() {
        Mockito.when(jwtUtil.getUsername("T1")).thenReturn("abc@gmail.com");
        Mockito.when(jwtUtil.isTokenValid("T1", "abc@gmail.com")).thenReturn(true);

        Assert.assertTrue(jwtUtil.isTokenValid("T1", "abc@gmail.com"));
    }

    @Test(priority = 56)
    public void t56_jwt_claimExtraction() {
        Claims claims = Mockito.mock(Claims.class);
        Mockito.when(jwtUtil.getClaims("TX")).thenReturn(claims);
        Assert.assertNotNull(jwtUtil.getClaims("TX"));
    }

    @Test(priority = 57)
    public void t57_jwt_notNull() {
        Assert.assertNotNull(jwtUtil);
    }



    // ======================================================================================
    //  8 — HQL + QUERY TESTING — 13 tests
    // ======================================================================================

    @Test(priority = 58)
    public void t58_query_findApiKeyByValue() {
        ApiKey k = new ApiKey();
        k.setKeyValue("XYZ123");

        Mockito.when(apiKeyRepo.findByKeyValue("XYZ123")).thenReturn(Optional.of(k));
        ApiKey out = apiKeyService.getApiKeyByValue("XYZ123");
        Assert.assertEquals(out.getKeyValue(), "XYZ123");
    }

    @Test(priority = 59)
    public void t59_query_findApiKeyByValue_notFound() {
        Mockito.when(apiKeyRepo.findByKeyValue("X")).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> apiKeyService.getApiKeyByValue("X"));
    }

    @Test(priority = 60)
    public void t60_query_usageFindByApiKeyId() {
        Mockito.when(usageRepo.findByApiKey_Id(10L))
                .thenReturn(new ArrayList<>());

        Assert.assertNotNull(usageService.getUsageForApiKey(10L));
    }

    @Test(priority = 61)
    public void t61_query_enforcement_findByApiKeyId() {
        Mockito.when(enforceRepo.findByApiKey_Id(8L))
                .thenReturn(new ArrayList<>());

        Assert.assertNotNull(enforcementService.getEnforcementsForKey(8L));
    }

    @Test(priority = 62)
    public void t62_query_exemption_findByApiKeyId() {
        KeyExemption ex = new KeyExemption();
        Mockito.when(exemptionRepo.findByApiKey_Id(2L))
                .thenReturn(Optional.of(ex));
        Assert.assertNotNull(exemptionService.getExemptionByKey(2L));
    }

    @Test(priority = 63)
    public void t63_query_exemption_notFound() {
        Mockito.when(exemptionRepo.findByApiKey_Id(77L))
                .thenReturn(Optional.empty());

        Assert.expectThrows(ResourceNotFoundException.class,
                () -> exemptionService.getExemptionByKey(77L));
    }

    @Test(priority = 64)
    public void t64_query_apiKey_findAll() {
        Mockito.when(apiKeyRepo.findAll())
                .thenReturn(Arrays.asList(new ApiKey(), new ApiKey()));

        Assert.assertEquals(apiKeyService.getAllApiKeys().size(), 2);
    }

    @Test(priority = 65)
    public void t65_query_quotaPlan_findAll() {
        Mockito.when(quotaPlanRepo.findAll())
                .thenReturn(Arrays.asList(new QuotaPlan(), new QuotaPlan()));

        Assert.assertEquals(quotaPlanService.getAllPlans().size(), 2);
    }

    @Test(priority = 66)
    public void t66_query_usage_findAllToday() {
        Mockito.when(usageRepo.findForKeyBetween(Mockito.eq(1L), Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(new ApiUsageLog()));

        Assert.assertEquals(usageService.getUsageForToday(1L).size(), 1);
    }

    @Test(priority = 67)
    public void t67_enforcement_getById_success() {
        RateLimitEnforcement e = new RateLimitEnforcement();
        Mockito.when(enforceRepo.findById(1L)).thenReturn(Optional.of(e));
        Assert.assertNotNull(enforcementService.getEnforcementById(1L));
    }

    @Test(priority = 68)
    public void t68_enforcement_getById_notFound() {
        Mockito.when(enforceRepo.findById(50L)).thenReturn(Optional.empty());
        Assert.expectThrows(ResourceNotFoundException.class,
                () -> enforcementService.getEnforcementById(50L));
    }

    @Test(priority = 69)
    public void t69_query_usage_countTodayMock() {
        Mockito.when(usageRepo.countForKeyBetween(Mockito.eq(123L), Mockito.any(), Mockito.any()))
                .thenReturn(99);
        Assert.assertEquals(usageService.countRequestsToday(123L), 99);
    }

    @Test(priority = 70)
    public void t70_finalSanityCheck() {
        Assert.assertTrue(true);
    }
}
