import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

@RunWith(Parameterized.class)
public class TestTakeOrder {
    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    List<String> color;

    public TestTakeOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] newOrderData() {
        return new Object[][]{
                {"Сергей", "Бушмакин", "Проспект В.123", "Воробьевы горы", "71002003344", 5, "2.02.2023", "Сдача с 5000", List.of("BLACK")},
                {"Сергей", "Бушмакин", "Проспект В.123", "Воробьевы горы", "71002003344", 5, "2.02.2023", "Сдача с 5000", List.of("BLACK", "GREY")},
                {"Сергей", "Бушмакин", "Проспект В.123", "Воробьевы горы", "71002003344", 5, "2.02.2023", "Сдача с 5000", List.of("GREY")},
                {"Сергей", "Бушмакин", "Проспект В.123", "Воробьевы горы", "71002003344", 5, "2.02.2023", "Сдача с 5000", null}
        };
    }

    @Test
    @DisplayName("Тест на создание заказа /api/v1/orders")
    @Description("Проверка успешного создания заказа")
    public void сreateOrderTrue() {
        Response response =sendGetRequest();
        compareResponse(response);
    }
    @Step("Шаг: Отправка запроса на создание заказа /api/v1/orders")
    public Response sendGetRequest(){
        DataOrder dataOrder = new DataOrder(firstName,lastName,address,metroStation,phone,rentTime, deliveryDate,comment, color);
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .body(dataOrder)
                .when()
                .post("/api/v1/orders");
        return response;
    }

    @Step("Шаг: Проверка тела ответа и статус кода /api/v1/orders")
    public void compareResponse(Response response){
        response.then().assertThat().body( "track", notNullValue())
                .and().statusCode(201 );
    }

}
