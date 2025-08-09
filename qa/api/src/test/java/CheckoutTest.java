import io.qameta.allure.*;               // NEW
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.testng.AllureTestNg;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("E‑Commerce")
@Feature("Checkout")
@Listeners({ AllureTestNg.class })

public class CheckoutTest {
    private static final String BASE = System.getenv().getOrDefault("BASE_URL", "http://localhost:3001");

    // Attach request/response to Allure
    private static final AllureRestAssured allureFilter = new AllureRestAssured()
            .setRequestTemplate("http-request.ftl").setResponseTemplate("http-response.ftl");

    @Story("Browse products")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void products_list() {
        given().filter(allureFilter)
        .when().get(BASE + "/products")
        .then().statusCode(200).body("size()", greaterThan(0));
    }

    @Story("Successful checkout")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void cart_and_checkout() {
        given().filter(allureFilter).contentType("application/json")
          .body("{\"sku\":\"SKU-100\",\"qty\":2}")
        .when().post(BASE + "/cart")
        .then().statusCode(200).body("ok", equalTo(true));

        given().filter(allureFilter)
        .when().post(BASE + "/checkout")
        .then().statusCode(200).body("status", equalTo("CONFIRMED"));
    }

    // ---------- NEGATIVE / EDGE CASES ----------

    @Story("Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void add_to_cart_requires_positive_qty() {
        given().filter(allureFilter).contentType("application/json")
          .body("{\"sku\":\"SKU-100\",\"qty\":0}")
        .when().post(BASE + "/cart")
        .then().statusCode(400).body("error", equalTo("qty_must_be_positive"));
    }

    @Story("Stock handling")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void add_to_cart_out_of_stock() {
        given().filter(allureFilter).contentType("application/json")
          .body("{\"sku\":\"SKU-100\",\"qty\":999}")
        .when().post(BASE + "/cart")
        .then().statusCode(409).body("error", equalTo("out_of_stock"));
    }

    @Story("Checkout validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void checkout_empty_cart_returns_400() {
        // Ensure cart is empty
        given().filter(allureFilter).when().post(BASE + "/cart/clear").then().statusCode(200);

        given().filter(allureFilter)
        .when().post(BASE + "/checkout")
        .then().statusCode(400).body("error", equalTo("empty_cart"));
    }

    @Story("Payload validation")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void add_to_cart_malformed_payload() {
        given().filter(allureFilter).contentType("application/json")
          .body("{\"sku\":\"SKU-DOES-NOT-EXIST\",\"qty\":1}")
        .when().post(BASE + "/cart")
        .then().statusCode(404).body("error", equalTo("not_found"));
    }
}
