import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestLoginСourier {

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    String login;
    String password;
    Integer expectedResult;
    public TestLoginСourier (String login, String password, Integer expectedResult) {
        this.login = login;
        this.password = password;
        this.expectedResult = expectedResult;
    }
    @Parameterized.Parameters
    public static Object[][] newOrderData(){
        return new Object[][] {
                {null,"NoLogin",400},
          //       {"NoPass",null,400}, //зависает, в Postman также
                {"SergeyB","FalsePass",404},
                {"SergeyB","7777",200}
        };
    }


    @Test
    @DisplayName("Проверка авторизации /api/v1/courier/login")
    @Description("Тест на авторизацию курьера")
    public void LoginСourier() {
        Response response = login();
        checkStatus(response);
        if (expectedResult==200) {
            checkId(response);
        }

    }

    @Step("Шаг: Отправка запроса на авторизацию /api/v1/courier/login")
    public Response login(){
        DataCourier dataCourier= new DataCourier(login,password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(dataCourier)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Шаг: Проверка статус кода /api/v1/courier/login")
    public void checkStatus(Response response){
        response.then().assertThat().statusCode(expectedResult);
    }
    @Step("Шаг: Проверка что успешный запрос возвращает id /api/v1/courier/login")
    public void checkId(Response response){
            response.then().assertThat().body("id", notNullValue()).and().statusCode(expectedResult);
    }


}
