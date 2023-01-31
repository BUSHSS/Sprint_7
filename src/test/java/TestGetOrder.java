import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class TestGetOrder {
    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Получение заказа /api/v1/orders")
    @Description("Тест на полученеи заказа")
     public void checkOrderTrue() {
        Response response = getList();
        checkList(response);
    }


    @Step("Шаг: Получение списка заказазов /api/v1/orders")
    public Response getList(){
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        return response;
    }

    @Step("Шаг: Проверка тела ответа и статус кода /api/v1/orders")
    public void checkList(Response response){
        response.then().assertThat().body( "orders", notNullValue())
                .and().statusCode(200 );
    }

}
