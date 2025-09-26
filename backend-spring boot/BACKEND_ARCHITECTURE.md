# E-Commerce Multi-Vendor Backend Architecture

## Overview

This is a comprehensive Spring Boot-based backend application for a multi-vendor e-commerce platform built using advanced Object-Oriented Programming principles and Enterprise Architecture patterns. The system supports multiple user roles (Admin, Seller, Customer) with complete e-commerce functionality including product management, order processing, payments, and AI-powered features.

## Technology Stack

### Core Framework

- **Spring Boot 3.3.2** - Main application framework with Inversion of Control (IoC)
- **Java 17** - Programming language with modern OOP features
- **Maven** - Build tool and dependency management

### Database & Persistence

- **MySQL 8.0** - Primary database
- **Spring Data JPA** - Data access layer with Repository Pattern
- **Hibernate 6.5.2** - Object-Relational Mapping (ORM) framework
- **HikariCP** - High-performance connection pooling

### Security

- **Spring Security 6** - Authentication and authorization framework
- **JWT (JSON Web Tokens)** - Stateless authentication mechanism
- **BCrypt** - Password encryption algorithm
- **Rate Limiting** - Custom implementation for API protection
- **CORS** - Cross-origin resource sharing configuration

### Payment Integration

- **Razorpay** - Indian payment gateway integration
- **Stripe** - International payment processing

### Communication & Messaging

- **Spring Mail** - Email service integration
- **SMTP** - Email protocol support

### AI Integration

- **Spring AI** - AI framework integration
- **Google Gemini API** - AI chatbot and product recommendations

### Documentation & Monitoring

- **OpenAPI 3** - API documentation standard
- **Swagger UI** - Interactive API documentation
- **Spring Boot Actuator** - Application monitoring and health checks

### Development Tools

- **Spring Boot DevTools** - Development productivity tools
- **Lombok** - Code generation and boilerplate reduction

## Object-Oriented Design Patterns Implementation

### 1. Model-View-Controller (MVC) Pattern

The application follows the Spring MVC architecture pattern:

```
┌─────────────────────────────────────────┐
│          Controller Layer               │  ← REST Controllers handle HTTP requests
│  @RestController, @RequestMapping       │
├─────────────────────────────────────────┤
│            Service Layer                │  ← Business logic and transaction management
│  @Service, @Transactional              │
├─────────────────────────────────────────┤
│          Repository Layer               │  ← Data access abstraction
│  @Repository, JpaRepository             │
├─────────────────────────────────────────┤
│            Model Layer                  │  ← JPA Entities and domain objects
│  @Entity, @Table                       │
└─────────────────────────────────────────┘
```

### 2. Dependency Injection Pattern

Spring Framework's core IoC container implements Dependency Injection:

```java
@Service
public class UserServiceImplementation implements UserService {
    
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    
    // Constructor-based Dependency Injection
    public UserServiceImplementation(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }
}
```

### 3. Repository Pattern

Spring Data JPA implements the Repository pattern for data access:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(USER_ROLE role);
    
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
}
```

### 4. Strategy Pattern

Payment processing uses Strategy pattern for different payment gateways:

```java
public interface PaymentStrategy {
    PaymentResponse processPayment(PaymentRequest request);
}

@Component
public class RazorpayStrategy implements PaymentStrategy {
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Razorpay specific implementation
    }
}

@Component
public class StripeStrategy implements PaymentStrategy {
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Stripe specific implementation
    }
}
```

### 5. Template Method Pattern

Spring Security configuration uses Template Method pattern:

```java
@Configuration
@EnableWebSecurity
public class AppConfig {
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(this::configureSessionManagement)
            .authorizeHttpRequests(this::configureAuthorization)
            .addFilterBefore(rateLimitingFilter(), BasicAuthenticationFilter.class)
            .build();
    }
    
    private void configureSessionManagement(SessionManagementConfigurer<HttpSecurity> session) {
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers("/auth/**").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated();
    }
}
```

### 6. Observer Pattern

Spring Events implement Observer pattern for loose coupling:

```java
@Component
public class OrderEventListener {
    
    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Send email notification
        // Update inventory
        // Generate invoice
    }
}
```

### 7. Singleton Pattern

Spring Beans are singleton by default:

```java
@Configuration
public class BeanConfiguration {
    
    @Bean
    @Scope("singleton") // Default scope
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 8. Factory Pattern

Spring Boot Auto-Configuration uses Factory pattern:

```java
@Configuration
@ConditionalOnClass(DataSource.class)
public class DatabaseConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

## Spring Boot Architecture Deep Dive

### 1. Inversion of Control (IoC) Container

Spring's IoC container manages object lifecycle and dependencies:

```java
@SpringBootApplication
public class EcommerceMultiVendorApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EcommerceMultiVendorApplication.class, args);
        // Container manages all beans automatically
    }
}
```

### 2. Aspect-Oriented Programming (AOP)

Cross-cutting concerns are handled through AOP:

```java
@Aspect
@Component
public class LoggingAspect {
    
    @Around("@annotation(Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }
}
```

### 3. Auto-Configuration

Spring Boot's auto-configuration reduces boilerplate:

```java
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ecom")
public class ApplicationConfiguration {
    // Automatic configuration based on classpath
}
```

## Hibernate ORM Implementation

### 1. Entity Mapping

JPA entities use object-relational mapping:

```java
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    
    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private USER_ROLE role;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_coupons",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Set<Coupon> usedCoupons = new HashSet<>();
}
```

### 2. Inheritance Mapping

Product hierarchy uses inheritance:

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
public abstract class BaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    // Common fields
}

@Entity
@DiscriminatorValue("PHYSICAL")
public class PhysicalProduct extends BaseProduct {
    private Double weight;
    private String dimensions;
}

@Entity
@DiscriminatorValue("DIGITAL")
public class DigitalProduct extends BaseProduct {
    private String downloadUrl;
    private String licenseKey;
}
```

### 3. Association Mapping

Complex relationships are mapped using JPA associations:

```java
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
```

## Spring Security Architecture

### 1. Security Filter Chain

Custom security configuration:

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/auth/**").permitAll();
                auth.requestMatchers("/admin/**").hasRole("ADMIN");
                auth.requestMatchers("/seller/**").hasAnyRole("SELLER", "ADMIN");
                auth.requestMatchers("/api/users/**").hasRole("CUSTOMER");
                auth.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> 
                ex.authenticationEntryPoint(customAuthenticationEntryPoint()))
            .build();
    }
}
```

### 2. Custom Authentication Provider

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());
        }
        
        throw new BadCredentialsException("Authentication failed");
    }
}
```

### 3. Method-Level Security

```java
@Service
@PreAuthorize("hasRole('ADMIN')")
public class AdminService {
    
    @PreAuthorize("hasRole('ADMIN') and #userId == authentication.principal.id")
    public void deleteUser(Long userId) {
        // Implementation
    }
    
    @PostAuthorize("returnObject.owner == authentication.name")
    public Order getOrder(Long orderId) {
        // Implementation
    }
}
```

## Advanced OOP Principles Implementation

### 1. SOLID Principles

#### Single Responsibility Principle (SRP)

```java
// Each service has a single responsibility
@Service
public class EmailService {
    public void sendEmail(String to, String subject, String body) {
        // Only handles email sending
    }
}

@Service
public class OrderProcessingService {
    public Order processOrder(OrderRequest request) {
        // Only handles order processing logic
    }
}
```

#### Open/Closed Principle (OCP)

```java
public abstract class PaymentProcessor {
    public abstract PaymentResult process(PaymentRequest request);
    
    // Template method - closed for modification, open for extension
    public final PaymentResponse handlePayment(PaymentRequest request) {
        validateRequest(request);
        PaymentResult result = process(request);
        logTransaction(result);
        return new PaymentResponse(result);
    }
}
```

#### Liskov Substitution Principle (LSP)

```java
public interface NotificationService {
    void send(String recipient, String message);
}

@Service
public class EmailNotificationService implements NotificationService {
    @Override
    public void send(String recipient, String message) {
        // Email implementation
    }
}

@Service
public class SMSNotificationService implements NotificationService {
    @Override
    public void send(String recipient, String message) {
        // SMS implementation
    }
}
```

#### Interface Segregation Principle (ISP)

```java
public interface ProductReader {
    Product findById(Long id);
    List<Product> findAll();
}

public interface ProductWriter {
    Product save(Product product);
    void delete(Long id);
}

public interface ProductSearch {
    List<Product> search(String query);
    List<Product> findByCategory(String category);
}

@Service
public class ProductService implements ProductReader, ProductWriter, ProductSearch {
    // Implementation
}
```

#### Dependency Inversion Principle (DIP)

```java
@Service
public class OrderService {
    
    // Depends on abstractions, not concretions
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    
    public OrderService(
            PaymentService paymentService,
            InventoryService inventoryService,
            NotificationService notificationService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
    }
}
```

### 2. Composition over Inheritance

```java
@Entity
public class Product {
    
    @Embedded
    private ProductDetails details;
    
    @Embedded
    private PricingInfo pricing;
    
    @Embedded
    private InventoryInfo inventory;
    
    // Composition instead of deep inheritance hierarchy
}

@Embeddable
public class ProductDetails {
    private String title;
    private String description;
    private String brand;
}

@Embeddable
public class PricingInfo {
    private BigDecimal mrpPrice;
    private BigDecimal sellingPrice;
    private Integer discountPercent;
}
```

### 3. Polymorphism Implementation

```java
public interface PriceCalculator {
    BigDecimal calculatePrice(Product product, User user);
}

@Component
public class RegularPriceCalculator implements PriceCalculator {
    @Override
    public BigDecimal calculatePrice(Product product, User user) {
        return product.getSellingPrice();
    }
}

@Component
public class MemberPriceCalculator implements PriceCalculator {
    @Override
    public BigDecimal calculatePrice(Product product, User user) {
        BigDecimal basePrice = product.getSellingPrice();
        return basePrice.multiply(BigDecimal.valueOf(0.9)); // 10% discount
    }
}

@Service
public class PricingService {
    
    private final Map<String, PriceCalculator> calculators;
    
    public PricingService(List<PriceCalculator> calculatorList) {
        this.calculators = calculatorList.stream()
            .collect(Collectors.toMap(
                calc -> calc.getClass().getSimpleName(),
                calc -> calc
            ));
    }
    
    public BigDecimal getPrice(Product product, User user) {
        PriceCalculator calculator = determineCalculator(user);
        return calculator.calculatePrice(product, user);
    }
}
```

## Architecture Overview

### Layered Architecture with Clean Architecture Principles

```text
┌─────────────────────────────────────────┐
│          Presentation Layer             │  ← Controllers, DTOs, Validation
│     @RestController, @Valid             │
├─────────────────────────────────────────┤
│           Application Layer             │  ← Use Cases, Application Services
│  @Service, @Transactional              │
├─────────────────────────────────────────┤
│            Domain Layer                 │  ← Business Logic, Domain Services
│  Domain Entities, Value Objects        │
├─────────────────────────────────────────┤
│         Infrastructure Layer           │  ← Repositories, External Services
│  @Repository, External APIs            │
└─────────────────────────────────────────┘
```

### Hexagonal Architecture Implementation

```text
    ┌─────────────────────────────────────┐
    │           Domain Core               │
    │                                     │
    │  ┌─────────────────────────────┐   │
    │  │     Business Logic          │   │
    │  │   Domain Entities           │   │
    │  │   Domain Services           │   │
    │  │   Value Objects             │   │
    │  └─────────────────────────────┘   │
    │                                     │
    └─────────────────────────────────────┘
              ↑                 ↑
    ┌─────────┴─────────┐ ┌─────┴─────────┐
    │   Primary Ports   │ │ Secondary Ports│
    │  (Driving)        │ │  (Driven)      │
    │                   │ │                │
    │ REST Controllers  │ │ Repositories   │
    │ GraphQL Resolvers │ │ Email Service  │
    │ Event Listeners   │ │ Payment APIs   │
    └───────────────────┘ └────────────────┘
```

## Project Structure

```text
src/main/java/com/ecom/
├── ai/                          # AI-powered features
│   ├── controllers/             # AI API endpoints (Facade Pattern)
│   └── services/               # AI business logic (Strategy Pattern)
├── config/                      # Configuration classes (Factory Pattern)
│   ├── AppConfig.java          # Security configuration (Template Method)
│   ├── JwtProvider.java        # JWT token management (Factory)
│   ├── RateLimitingConfig.java # API rate limiting (Decorator Pattern)
│   └── SecurityEnhancementConfig.java
├── controller/                  # REST API controllers (Controller Pattern)
├── domain/                      # Enums and domain constants (Value Objects)
├── dto/                        # Data transfer objects (DTO Pattern)
├── exception/                  # Custom exceptions and handlers
├── mapper/                     # Entity-DTO mappers (Adapter Pattern)
├── model/                      # JPA entities (Domain Model Pattern)
├── repository/                 # Data access repositories (Repository Pattern)
├── request/                    # Request DTOs (Command Pattern)
├── response/                   # Response DTOs (Data Transfer Pattern)
├── security/                   # Security utilities (Utility Pattern)
├── service/                    # Business logic services (Service Layer Pattern)
│   └── impl/                   # Service implementations (Strategy Pattern)
└── utils/                      # Utility classes (Utility Pattern)
```

## Advanced Design Patterns in Implementation

### 1. Builder Pattern for Complex Objects

```java
@Component
public class OrderBuilder {
    
    private Order order;
    
    public OrderBuilder createNew() {
        this.order = new Order();
        return this;
    }
    
    public OrderBuilder withUser(User user) {
        order.setUser(user);
        return this;
    }
    
    public OrderBuilder withItems(List<OrderItem> items) {
        order.setOrderItems(items);
        return this;
    }
    
    public OrderBuilder withShippingAddress(Address address) {
        order.setShippingAddress(address);
        return this;
    }
    
    public OrderBuilder withPaymentMethod(PaymentMethod method) {
        order.setPaymentMethod(method);
        return this;
    }
    
    public Order build() {
        validateOrder(order);
        calculateTotal(order);
        return order;
    }
}
```

### 2. Decorator Pattern for Service Enhancement

```java
public interface ProductService {
    Product findById(Long id);
    Product save(Product product);
}

@Service
@Primary
public class CachedProductService implements ProductService {
    
    private final ProductService delegate;
    private final CacheManager cacheManager;
    
    public CachedProductService(
            @Qualifier("basicProductService") ProductService delegate,
            CacheManager cacheManager) {
        this.delegate = delegate;
        this.cacheManager = cacheManager;
    }
    
    @Override
    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        return delegate.findById(id);
    }
    
    @Override
    @CacheEvict(value = "products", key = "#product.id")
    public Product save(Product product) {
        return delegate.save(product);
    }
}
```

### 3. Command Pattern for Order Processing

```java
public interface OrderCommand {
    void execute();
    void undo();
}

@Component
public class CreateOrderCommand implements OrderCommand {
    
    private final Order order;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private boolean executed = false;
    
    @Override
    public void execute() {
        if (!executed) {
            inventoryService.reserveItems(order.getOrderItems());
            orderRepository.save(order);
            executed = true;
        }
    }
    
    @Override
    public void undo() {
        if (executed) {
            inventoryService.releaseItems(order.getOrderItems());
            orderRepository.delete(order);
            executed = false;
        }
    }
}

@Service
public class OrderProcessor {
    
    private final Stack<OrderCommand> commandHistory = new Stack<>();
    
    public void processOrder(OrderCommand command) {
        try {
            command.execute();
            commandHistory.push(command);
        } catch (Exception e) {
            // Rollback if needed
            command.undo();
            throw e;
        }
    }
    
    public void undoLastOperation() {
        if (!commandHistory.isEmpty()) {
            OrderCommand lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }
}
```

### 4. Specification Pattern for Complex Queries

```java
public interface Specification<T> {
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}

public class ProductSpecification {
    
    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) -> 
            cb.equal(root.get("category").get("name"), category);
    }
    
    public static Specification<Product> hasPriceRange(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> 
            cb.between(root.get("sellingPrice"), min, max);
    }
    
    public static Specification<Product> isInStock() {
        return (root, query, cb) -> 
            cb.isTrue(root.get("inStock"));
    }
    
    public static Specification<Product> hasRatingAbove(double rating) {
        return (root, query, cb) -> 
            cb.greaterThan(root.get("averageRating"), rating);
    }
}

@Service
public class ProductSearchService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> searchProducts(ProductSearchCriteria criteria) {
        Specification<Product> spec = Specification.where(null);
        
        if (criteria.getCategory() != null) {
            spec = spec.and(ProductSpecification.hasCategory(criteria.getCategory()));
        }
        
        if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null) {
            spec = spec.and(ProductSpecification.hasPriceRange(
                criteria.getMinPrice(), criteria.getMaxPrice()));
        }
        
        if (criteria.isInStockOnly()) {
            spec = spec.and(ProductSpecification.isInStock());
        }
        
        return productRepository.findAll(spec);
    }
}
```

### 5. Chain of Responsibility for Order Validation

```java
public abstract class OrderValidationHandler {
    
    protected OrderValidationHandler nextHandler;
    
    public void setNext(OrderValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    public abstract ValidationResult validate(Order order);
    
    protected ValidationResult validateNext(Order order) {
        if (nextHandler != null) {
            return nextHandler.validate(order);
        }
        return ValidationResult.success();
    }
}

@Component
public class InventoryValidationHandler extends OrderValidationHandler {
    
    @Autowired
    private InventoryService inventoryService;
    
    @Override
    public ValidationResult validate(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            if (!inventoryService.isAvailable(item.getProduct(), item.getQuantity())) {
                return ValidationResult.failure("Insufficient inventory for " + item.getProduct().getTitle());
            }
        }
        return validateNext(order);
    }
}

@Component
public class PaymentValidationHandler extends OrderValidationHandler {
    
    @Autowired
    private PaymentService paymentService;
    
    @Override
    public ValidationResult validate(Order order) {
        if (!paymentService.isPaymentMethodValid(order.getPaymentMethod())) {
            return ValidationResult.failure("Invalid payment method");
        }
        return validateNext(order);
    }
}

@Service
public class OrderValidationService {
    
    private final OrderValidationHandler validationChain;
    
    public OrderValidationService(
            InventoryValidationHandler inventoryHandler,
            PaymentValidationHandler paymentHandler,
            UserValidationHandler userHandler) {
        
        // Build the chain
        inventoryHandler.setNext(paymentHandler);
        paymentHandler.setNext(userHandler);
        this.validationChain = inventoryHandler;
    }
    
    public ValidationResult validateOrder(Order order) {
        return validationChain.validate(order);
    }
}
```

## Spring Framework Core Concepts

### 1. Bean Lifecycle Management

```java
@Component
public class ProductCacheManager implements InitializingBean, DisposableBean {
    
    private Cache<Long, Product> productCache;
    
    @Override
    public void afterPropertiesSet() {
        // Initialize cache after bean creation
        this.productCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
        
        logger.info("Product cache initialized");
    }
    
    @Override
    public void destroy() {
        // Cleanup resources before bean destruction
        if (productCache != null) {
            productCache.invalidateAll();
            logger.info("Product cache destroyed");
        }
    }
    
    @PostConstruct
    public void init() {
        logger.info("ProductCacheManager bean created");
    }
    
    @PreDestroy
    public void cleanup() {
        logger.info("ProductCacheManager bean destroying");
    }
}
```

### 2. Event-Driven Architecture

```java
// Custom Application Events
public class OrderCreatedEvent extends ApplicationEvent {
    private final Order order;
    
    public OrderCreatedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
    
    public Order getOrder() {
        return order;
    }
}

// Event Publisher
@Service
public class OrderService {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = buildOrder(request);
        order = orderRepository.save(order);
        
        // Publish event for other components to handle
        eventPublisher.publishEvent(new OrderCreatedEvent(this, order));
        
        return order;
    }
}

// Event Listeners
@Component
public class OrderEventHandlers {
    
    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        Order order = event.getOrder();
        
        // Send confirmation email
        emailService.sendOrderConfirmation(order);
        
        // Update analytics
        analyticsService.recordOrderCreated(order);
        
        // Notify inventory system
        inventoryService.updateInventory(order.getOrderItems());
    }
    
    @EventListener
    @Conditional(OrderValueCondition.class)
    public void handleHighValueOrder(OrderCreatedEvent event) {
        // Special handling for high-value orders
        if (event.getOrder().getTotalAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            adminNotificationService.notifyHighValueOrder(event.getOrder());
        }
    }
}
```

### 3. Conditional Bean Creation

```java
@Configuration
public class PaymentConfiguration {
    
    @Bean
    @ConditionalOnProperty(name = "payment.razorpay.enabled", havingValue = "true")
    public RazorpayPaymentService razorpayPaymentService() {
        return new RazorpayPaymentService();
    }
    
    @Bean
    @ConditionalOnProperty(name = "payment.stripe.enabled", havingValue = "true")
    public StripePaymentService stripePaymentService() {
        return new StripePaymentService();
    }
    
    @Bean
    @ConditionalOnMissingBean(PaymentService.class)
    public MockPaymentService mockPaymentService() {
        return new MockPaymentService();
    }
}
```

## Hibernate Advanced Features

### 1. Custom Converters

```java
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {
    
    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money != null ? money.getAmount() : null;
    }
    
    @Override
    public Money convertToEntityAttribute(BigDecimal amount) {
        return amount != null ? new Money(amount) : null;
    }
}

@Entity
public class Product {
    
    @Convert(converter = MoneyConverter.class)
    private Money price;
    
    @Convert(converter = JsonConverter.class)
    private ProductMetadata metadata;
}
```

### 2. Entity Listeners

```java
@EntityListeners(AuditingEntityListener.class)
@Entity
public class BaseEntity {
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}

@Component
public class CustomEntityListener {
    
    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable) {
            ((Auditable) entity).setCreatedAt(LocalDateTime.now());
        }
    }
    
    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable) {
            ((Auditable) entity).setUpdatedAt(LocalDateTime.now());
        }
    }
}
```

### 3. Custom Queries and Projections

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE p.category.name = :category " +
           "AND p.sellingPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndPriceRange(
        @Param("category") String category,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice);
    
    @Query(value = "SELECT p.id, p.title, p.selling_price, AVG(r.rating) as avg_rating " +
                   "FROM product p LEFT JOIN review r ON p.id = r.product_id " +
                   "GROUP BY p.id, p.title, p.selling_price " +
                   "HAVING AVG(r.rating) >= :minRating", nativeQuery = true)
    List<ProductSummary> findProductsWithHighRating(@Param("minRating") double minRating);
    
    // Using Projections
    @Query("SELECT new com.ecom.dto.ProductDTO(p.id, p.title, p.sellingPrice, c.name) " +
           "FROM Product p JOIN p.category c WHERE p.seller.id = :sellerId")
    List<ProductDTO> findProductDTOsBySeller(@Param("sellerId") Long sellerId);
}

// Projection Interface
public interface ProductSummary {
    Long getId();
    String getTitle();
    BigDecimal getSellingPrice();
    Double getAvgRating();
}
```

## Spring Security Advanced Implementation

### 1. Custom Security Expressions

```java
@Component("securityService")
public class CustomSecurityService {
    
    public boolean isOwner(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId().equals(userId);
    }
    
    public boolean canAccessOrder(Authentication authentication, Long orderId) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        
        // Admin can access all orders
        if (principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // Users can only access their own orders
        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null && order.getUser().getId().equals(principal.getId());
    }
}

// Usage in Controllers
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @GetMapping("/{orderId}")
    @PreAuthorize("@securityService.canAccessOrder(authentication, #orderId)")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        // Implementation
    }
    
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or (@securityService.isOwner(authentication, #order.userId) and #order.status == 'PENDING')")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequest request) {
        // Implementation
    }
}
```

### 2. Multiple Authentication Providers

```java
@Configuration
public class MultipleAuthenticationProvidersConfig {
    
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            DatabaseAuthenticationProvider databaseProvider,
            LdapAuthenticationProvider ldapProvider) throws Exception {
        
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(databaseProvider)
            .authenticationProvider(ldapProvider)
            .build();
    }
}

@Component
public class DatabaseAuthenticationProvider implements AuthenticationProvider {
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Database authentication logic
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // LDAP authentication logic
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

## Performance Optimization Patterns

### 1. N+1 Query Problem Solution

```java
@Entity
public class Order {
    
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Solution using @EntityGraph
    @NamedEntityGraph(
        name = "Order.withItems",
        attributeNodes = @NamedAttributeNode("orderItems")
    )
    public static class OrderWithItems {}
}

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @EntityGraph("Order.withItems")
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findOrdersWithItemsByUserId(@Param("userId") Long userId);
    
    // Using JOIN FETCH
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems WHERE o.status = :status")
    List<Order> findOrdersWithItemsByStatus(@Param("status") OrderStatus status);
}
```

### 2. Caching Strategies

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.Builder builder = RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(cacheConfiguration());
        
        return builder.build();
    }
    
    private RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}

@Service
public class ProductService {
    
    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
    
    @CachePut(value = "products", key = "#product.id")
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    @CacheEvict(value = "products", key = "#id")
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public void clearCache() {
        // Clear all product cache entries
    }
}
```

This comprehensive architecture demonstrates how Object-Oriented Programming principles, Spring Boot framework features, Hibernate ORM capabilities, and Spring Security mechanisms work together to create a robust, scalable, and maintainable e-commerce platform.

## Core Modules

### 1. User Management
- **Multi-role system**: Admin, Seller, Customer
- **JWT-based authentication**
- **Email verification**
- **Password reset functionality**
- **Profile management**

### 2. Product Management
- **Category hierarchy** (3-level deep)
- **Product CRUD operations**
- **Image management**
- **Inventory tracking**
- **Product search and filtering**
- **Reviews and ratings**

### 3. Order Management
- **Shopping cart functionality**
- **Order processing workflow**
- **Order status tracking**
- **Order history**
- **Invoice generation**

### 4. Payment System
- **Multiple payment gateways** (Razorpay, Stripe)
- **Payment status tracking**
- **Refund management**
- **Transaction history**

### 5. Seller Portal
- **Seller registration and verification**
- **Product management**
- **Order fulfillment**
- **Revenue tracking**
- **Payout management**

### 6. Admin Panel
- **User management**
- **Seller verification**
- **Product moderation**
- **Order oversight**
- **Revenue analytics**
- **Coupon management**

### 7. AI Features
- **Chatbot integration**
- **Product recommendations**
- **Smart search**
- **Product details assistance**

## Security Features

### Authentication & Authorization
- **JWT Token-based authentication**
- **Role-based access control (RBAC)**
- **Stateless session management**
- **Password encryption with BCrypt**

### API Security
- **Rate limiting** (10 requests/minute for auth endpoints, 100 for others)
- **CORS configuration**
- **Security headers** (HSTS, X-Frame-Options, etc.)
- **Input validation**

### Data Protection
- **SQL injection prevention**
- **XSS protection**
- **CSRF protection disabled** (stateless API)
- **Secure cookie handling**

## Database Design

### Key Entities

#### User Management
- `User` - Core user entity
- `VerificationCode` - Email verification
- `PasswordResetToken` - Password reset tokens

#### Product Catalog
- `Category` - Product categories (hierarchical)
- `Product` - Product information
- `Review` - Product reviews and ratings

#### E-commerce Core
- `Cart` & `CartItem` - Shopping cart
- `Order` & `OrderItem` - Order management
- `Wishlist` - User wishlists

#### Seller Management
- `Seller` - Seller profiles
- `SellerReport` - Seller analytics
- `BusinessDetails` & `BankDetails` - Seller information

#### Payment & Transactions
- `PaymentOrder` - Payment processing
- `Transaction` - Transaction records
- `Payouts` - Seller payouts

#### Marketing & Promotions
- `Coupon` - Discount coupons
- `Deal` - Special deals
- `Home` & `HomeCategory` - Homepage content

## API Endpoints

### Authentication (`/auth/**`)
- `POST /auth/signup` - User registration
- `POST /auth/signin` - User login
- `POST /auth/send-otp` - Send OTP for verification
- `POST /auth/verify-otp` - Verify OTP

### Admin APIs (`/api/admin/**`)
- User management
- Seller verification
- Product moderation
- Order management
- Analytics and reporting

### Seller APIs (`/api/seller/**`)
- Product management
- Order fulfillment
- Revenue tracking
- Profile management

### Customer APIs
- `GET /api/products/**` - Product browsing
- `POST /api/cart/**` - Cart management
- `POST /api/orders/**` - Order placement
- `GET /api/reviews/**` - Review management

### AI APIs (`/ai/**`)
- Chatbot interactions
- Product recommendations
- Smart search

## Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_multi_vendor
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Security Configuration
- JWT secret management
- Token expiration settings
- Role-based access rules

### External Service Configuration
- Payment gateway credentials
- Email service settings
- AI service API keys

## Deployment

### Docker Support
- **Multi-stage Dockerfile** for optimized builds
- **Docker Compose** for development environment
- **Health checks** and monitoring
- **Non-root user** for security

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_multi_vendor
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Payment Gateways
RAZORPAY_KEY=your_razorpay_key
RAZORPAY_SECRET=your_razorpay_secret
STRIPE_KEY=your_stripe_key

# Email Service
MAIL_USERNAME=your_email
MAIL_PASSWORD=your_app_password

# AI Service
GEMINI_API_KEY=your_gemini_key
```

## Development Workflow

### Running Locally
```bash
# Using Maven
mvn spring-boot:run

# Using Docker Compose
docker-compose up -d
```

### Building for Production
```bash
# Maven build
mvn clean package

# Docker build
docker build -t ecommerce-backend .
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=TestClassName
```

## Monitoring & Observability

### Health Checks
- `/actuator/health` - Application health status
- Database connectivity checks
- External service availability

### Logging
- Structured logging with appropriate levels
- Request/response logging
- Error tracking and alerting

### Metrics
- Application performance metrics
- Business metrics (orders, revenue)
- Technical metrics (response times, error rates)

## Performance Considerations

### Database Optimization
- **Connection pooling** with HikariCP
- **Query optimization** with proper indexing
- **Lazy loading** for entity relationships

### Caching Strategy
- Application-level caching for frequently accessed data
- Database query result caching
- Static content caching

### Scalability
- **Stateless design** for horizontal scaling
- **Microservice-ready** architecture
- **Load balancer** compatible

## Security Best Practices

### Code Security
- Input validation and sanitization
- Parameterized queries to prevent SQL injection
- Secure credential management
- Regular dependency updates

### Runtime Security
- Non-root container execution
- Resource limits and quotas
- Network isolation
- Regular security scans

## Future Enhancements

### Planned Features
- **Microservices migration** for better scalability
- **Event-driven architecture** with message queues
- **Advanced analytics** and reporting
- **Mobile API optimization**
- **Multi-tenant support**

### Technical Improvements
- **Redis caching** integration
- **Elasticsearch** for advanced search
- **GraphQL** API support
- **Real-time notifications** with WebSockets

## Troubleshooting

### Common Issues
1. **Database connection errors** - Check MySQL service and credentials
2. **JWT token issues** - Verify token generation and validation
3. **Payment gateway failures** - Check API keys and network connectivity
4. **Email service problems** - Verify SMTP settings

### Debug Mode
```bash
# Enable debug logging
java -jar app.jar --logging.level.com.zosh=DEBUG
```

## Support and Maintenance

### Development Team
- Backend development and maintenance
- Database administration
- DevOps and deployment
- Security and compliance

### Documentation
- API documentation available at `/swagger-ui.html`
- Database schema documentation
- Deployment guides
- Security guidelines

## Conclusion

This backend architecture provides a robust, scalable, and secure foundation for a multi-vendor e-commerce platform. The modular design allows for easy maintenance and future enhancements while maintaining high performance and security standards.