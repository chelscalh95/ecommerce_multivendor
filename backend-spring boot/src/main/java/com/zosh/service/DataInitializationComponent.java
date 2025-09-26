package com.zosh.service;

import com.zosh.domain.USER_ROLE;
import com.zosh.domain.AccountStatus;
import com.zosh.model.*;
import com.zosh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final DealRepository dealRepository;

    @Override
    public void run(String... args) {
        initializeAdminUser();
        initializeCategories();
        initializeSellers();
        initializeCustomers();
        initializeProducts();
        // initializeCoupons();
        // initializeDeals();
    }

    private void initializeAdminUser() {
        String adminUsername = "admin@ecommerce.com";

        if (userRepository.findByEmail(adminUsername) == null) {
            User adminUser = new User();
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFullName("Admin User");
            adminUser.setEmail(adminUsername);
            adminUser.setMobile("1234567890");
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);
            userRepository.save(adminUser);
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            // Electronics
            Category electronics = new Category();
            electronics.setName("Electronics");
            electronics.setCategoryId("electronics");
            electronics.setLevel(1);
            electronics = categoryRepository.save(electronics);

            Category mobiles = new Category();
            mobiles.setName("Mobile Phones");
            mobiles.setCategoryId("mobiles");
            mobiles.setLevel(2);
            mobiles.setParentCategory(electronics);
            categoryRepository.save(mobiles);

            Category laptops = new Category();
            laptops.setName("Laptops");
            laptops.setCategoryId("laptops");
            laptops.setLevel(2);
            laptops.setParentCategory(electronics);
            categoryRepository.save(laptops);

            // Fashion
            Category fashion = new Category();
            fashion.setName("Fashion");
            fashion.setCategoryId("fashion");
            fashion.setLevel(1);
            fashion = categoryRepository.save(fashion);

            Category mensFashion = new Category();
            mensFashion.setName("Men's Fashion");
            mensFashion.setCategoryId("mens-fashion");
            mensFashion.setLevel(2);
            mensFashion.setParentCategory(fashion);
            categoryRepository.save(mensFashion);
        }
    }

    private void initializeSellers() {
        if (sellerRepository.count() == 0) {
            // Create seller users first
            User seller1User = createSellerUser("seller1@ecommerce.com", "Seller One", "seller123");
            User seller2User = createSellerUser("seller2@ecommerce.com", "Seller Two", "seller123");

            // Create sellers
            Seller seller1 = new Seller();
            seller1.setSellerName("TechWorld Store");
            seller1.setEmail("seller1@ecommerce.com");
            seller1.setPassword(passwordEncoder.encode("seller123"));
            seller1.setMobile("9876543210");
            seller1.setAccountStatus(AccountStatus.ACTIVE);

            BusinessDetails business1 = new BusinessDetails();
            business1.setBusinessName("TechWorld LLC");
            business1.setBusinessEmail("business@techworld.com");
            business1.setBusinessMobile("9876543210");
            business1.setBusinessAddress("123 Business St, NY 10001");
            business1.setLogo("https://example.com/logo1.png");
            business1.setBanner("https://example.com/banner1.png");
            seller1.setBusinessDetails(business1);

            BankDetails bank1 = new BankDetails();
            bank1.setAccountNumber("1234567890");
            bank1.setAccountHolderName("TechWorld LLC");
            bank1.setIfscCode("TECH001");
            seller1.setBankDetails(bank1);

            sellerRepository.save(seller1);

            Seller seller2 = new Seller();
            seller2.setSellerName("Fashion Hub");
            seller2.setEmail("seller2@ecommerce.com");
            seller2.setPassword(passwordEncoder.encode("seller123"));
            seller2.setMobile("8765432109");
            seller2.setAccountStatus(AccountStatus.ACTIVE);

            BusinessDetails business2 = new BusinessDetails();
            business2.setBusinessName("Fashion Hub Inc");
            business2.setBusinessEmail("business@fashionhub.com");
            business2.setBusinessMobile("8765432109");
            business2.setBusinessAddress("456 Fashion St, CA 90001");
            business2.setLogo("https://example.com/logo2.png");
            business2.setBanner("https://example.com/banner2.png");
            seller2.setBusinessDetails(business2);

            BankDetails bank2 = new BankDetails();
            bank2.setAccountNumber("0987654321");
            bank2.setAccountHolderName("Fashion Hub Inc");
            bank2.setIfscCode("FASH001");
            seller2.setBankDetails(bank2);

            sellerRepository.save(seller2);
        }
    }

    private User createSellerUser(String email, String fullName, String password) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(USER_ROLE.ROLE_SELLER);
        return userRepository.save(user);
    }

    private void initializeCustomers() {
        if (userRepository.findByEmail("customer1@test.com") == null) {
            User customer1 = new User();
            customer1.setEmail("customer1@test.com");
            customer1.setFullName("John Doe");
            customer1.setMobile("5551234567");
            customer1.setPassword(passwordEncoder.encode("customer123"));
            customer1.setRole(USER_ROLE.ROLE_CUSTOMER);
            userRepository.save(customer1);

            User customer2 = new User();
            customer2.setEmail("customer2@test.com");
            customer2.setFullName("Jane Smith");
            customer2.setMobile("5559876543");
            customer2.setPassword(passwordEncoder.encode("customer123"));
            customer2.setRole(USER_ROLE.ROLE_CUSTOMER);
            userRepository.save(customer2);
        }
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            Category mobileCategory = categoryRepository.findByCategoryId("mobiles");
            Category laptopCategory = categoryRepository.findByCategoryId("laptops");
            Category mensFashionCategory = categoryRepository.findByCategoryId("mens-fashion");

            Seller seller1 = sellerRepository.findByEmail("seller1@ecommerce.com");
            Seller seller2 = sellerRepository.findByEmail("seller2@ecommerce.com");

            if (seller1 != null && seller2 != null) {
                // Mobile Products
                createProduct("iPhone 15 Pro", "Latest iPhone with advanced features", 99999, 89999,
                        Arrays.asList("https://example.com/iphone1.jpg", "https://example.com/iphone2.jpg"),
                        mobileCategory, seller1, "Space Black", 50);

                createProduct("Samsung Galaxy S24", "Flagship Android smartphone", 79999, 69999,
                        Arrays.asList("https://example.com/samsung1.jpg", "https://example.com/samsung2.jpg"),
                        mobileCategory, seller1, "Phantom Black", 30);

                // Laptop Products
                createProduct("MacBook Air M3", "Powerful and lightweight laptop", 119999, 109999,
                        Arrays.asList("https://example.com/macbook1.jpg", "https://example.com/macbook2.jpg"),
                        laptopCategory, seller1, "Silver", 25);

                createProduct("Dell XPS 13", "Premium ultrabook", 89999, 79999,
                        Arrays.asList("https://example.com/dell1.jpg", "https://example.com/dell2.jpg"),
                        laptopCategory, seller2, "Black", 20);

                // Fashion Products
                createProduct("Men's Cotton T-Shirt", "Comfortable cotton t-shirt", 1299, 999,
                        Arrays.asList("https://example.com/tshirt1.jpg", "https://example.com/tshirt2.jpg"),
                        mensFashionCategory, seller2, "Blue", 100);

                createProduct("Men's Denim Jeans", "Classic denim jeans", 2999, 2499,
                        Arrays.asList("https://example.com/jeans1.jpg", "https://example.com/jeans2.jpg"),
                        mensFashionCategory, seller2, "Blue", 75);
            }
        }
    }

    private void createProduct(String title, String description, int mrp, int sellingPrice,
            java.util.List<String> images, Category category, Seller seller,
            String color, int quantity) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setMrpPrice(mrp);
        product.setSellingPrice(sellingPrice);
        product.setDiscountPercent((int) (((double) (mrp - sellingPrice) / mrp) * 100));
        product.setImages(images);
        product.setCategory(category);
        product.setSeller(seller);
        product.setColor(color);
        product.setQuantity(quantity);
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    private void initializeCoupons() {
        if (couponRepository.count() == 0) {
            Coupon coupon1 = new Coupon();
            coupon1.setCode("WELCOME20");
            coupon1.setDiscountPercentage(20);
            coupon1.setMinimumOrderValue(BigDecimal.valueOf(1000).doubleValue());
            coupon1.setValidityStartDate(LocalDate.now());
            coupon1.setValidityEndDate(LocalDate.now().plusDays(30));
            coupon1.setActive(true);
            couponRepository.save(coupon1);

            Coupon coupon2 = new Coupon();
            coupon2.setCode("SAVE500");
            coupon2.setDiscountPercentage(10);
            coupon2.setMinimumOrderValue(2000.0);
            coupon2.setValidityStartDate(LocalDate.now());
            coupon2.setValidityEndDate(LocalDate.now().plusDays(60));
            coupon2.setActive(true);
            couponRepository.save(coupon2);
        }
    }

    private void initializeDeals() {
        if (dealRepository.count() == 0) {
            Category electronics = categoryRepository.findByCategoryId("electronics");
            if (electronics != null) {
                Deal deal1 = new Deal();
                deal1.setDiscount(25);
                // deal1.setCategory(electronics); // Fix: Category type mismatch - Deal expects HomeCategory
                dealRepository.save(deal1);
            }
        }
    }
}