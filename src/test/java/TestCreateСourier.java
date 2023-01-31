
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class TestCreateСourier {



    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    String login;
    String password;
    Integer expectedResult;
    public TestCreateСourier (String login, String password, Integer expectedResult) {
        this.login = login;
        this.password = password;
        this.expectedResult=expectedResult;
    }
    @Parameterized.Parameters
    public static Object[][] newOrderData(){
        return new Object[][] {
                {null,"12345",400},
                {"Bush",null,400},
                {"SergeyB","7777",201},
        };
    }


    @Test
    @DisplayName("Создание курьера /v1/courier")
    @Description("Проверка создания курьера")
    public void сreateСourierTrue() {

        DataCourier dataCourier = newLogin();
        Response response = sendGetRequest(dataCourier);
        compareResponse(response);
        if (expectedResult==201)
            {
                compareBody(response);
                createCopy(dataCourier);
                deleteData(dataCourier);
            }
    }

    @Step("Шаг: генерация нового логина")
    public DataCourier newLogin(){
        if (login != null) {
            login=login+new Random().nextInt(1000);}
        DataCourier dataCourier= new DataCourier(login,password);
        return dataCourier;
    }

    @Step("Шаг: Отправка запроса на создание курьера /api/v1/courier")
    public Response sendGetRequest(DataCourier dataCourier){
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(dataCourier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Шаг: Проверка тела ответа и статус кода /api/v1/courier")
    public void compareResponse(Response response){
        response.then().assertThat().statusCode(expectedResult);
    }

    @Step("Шаг: Проверка, что успешный ответ содержит ок /api/v1/courier")
    public void compareBody(Response response){
            response.then().assertThat().body("ok", equalTo(true)).and().statusCode(expectedResult);
    }

    @Step("Шаг: Проверить, что нельзя создать курьера с тем же логином и возвращается ошибка /api/v1/courier")
    public void createCopy(DataCourier dataCourier){
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(dataCourier)
                    .when()
                    .post("/api/v1/courier").then().statusCode(409);
    }

    @Step("Шаг: Удаление созданного курьера")
    public void deleteData(DataCourier dataCourier) {

        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(dataCourier)
                .when()
                .post("/api/v1/courier/login");

           if (response2.statusCode() == 200) {
               int id = response2.then().extract().body().path("id");
               String idJson="{\"id\" : "+id+"}";
            given()
                    .header("Content-type", "application/json")
                    .and().body(idJson)
                    .delete("/api/v1/courier/"+id);

        }
    }


}
