
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Random;

import static io.restassured.RestAssured.*;

@RunWith(Parameterized.class)
public class TestCreateСourier {

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    String login;
    String password;
    String firstName;
    public TestCreateСourier (String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    @Parameterized.Parameters
    public static Object[][] newOrderData(){
        return new Object[][] {
                {null,"12345","Бушмакин"},
                {"Bush",null,""},
                {"SergeyB","7777","Бушмакин"}
        };
    }


    @Test
    public void сreateСourierTrue() {

        TestDataCourier  dataCourier= new TestDataCourier(login,password,firstName);
        if (login != null) {
            login=login+new Random().nextInt(1000);}

        if (login == null || password == null) {

            given()
                    .header("Content-type", "application/json")
                    .and()
                    // сюда передали созданный обьект с нужными значениями полей
                    .body(dataCourier)
                    .when()
                    .post("/api/v1/courier").then().statusCode(400);
        } else {
            // Проверить, что курьер со всеми обязательными полями создается
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(dataCourier)
                    .when()
                    .post("/api/v1/courier").then().statusCode(201);


            //Проверить, что нельзя создать курьера с тем же логином и возвращается ошибка
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(dataCourier)
                    .when()
                    .post("/api/v1/courier").then().statusCode(409);
        }
    }

}
