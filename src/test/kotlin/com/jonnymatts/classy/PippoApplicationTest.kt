package com.jonnymatts.classy

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.ClassRule
import org.junit.Test
import ro.pippo.test.PippoRule
import ro.pippo.test.PippoTest

class PippoApplicationTest : PippoTest() {

    companion object PippoApplicationTest {
        @JvmField
        @ClassRule
        val pippoRule = PippoRule(PippoApplication(ClassSearcher(), Randomizer()))
    }

    @Test
    fun testRandomClass() {
        val pippoResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("package", "com.jonnymatts.classy")
                .get("/randomClass")
        pippoResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("package", equalTo("com.jonnymatts.classy"))
                .body("name", isA(String::class.java))
    }

    @Test
    fun randomClassReturnsBadRequestIfNoClassesAreFound() {
        val pippoResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("package", "com.jonnymatts.classi")
                .get("/randomClass")
        pippoResponse.then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", containsString("com.jonnymatts.classi"))
    }
}