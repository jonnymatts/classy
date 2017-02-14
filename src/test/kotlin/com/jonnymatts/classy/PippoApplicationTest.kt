package com.jonnymatts.classy

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import ro.pippo.test.PippoRule
import ro.pippo.test.PippoTest

class PippoApplicationTest : PippoTest() {

    companion object PippoApplicationTest {

        val loadedClasses: List<ClassInfo> = listOf(
            ClassInfo("Blah","com.jonnymatts.classy"),
            ClassInfo("Foo","com.jonnymatts.classy")
        )

        val packageList: MutableList<Package> = mutableListOf()
        val packages: Packages = Packages(packageList)

        @JvmField
        @ClassRule
        val pippoRule = PippoRule(PippoApplication(packages, Randomizer()))
    }

    @Before
    fun setUp() {
        packageList.clear()
    }

    @Test
    fun testRandomClass() {
        packageList.add(
                Package(
                        "com",
                        mutableListOf(
                                Package("jonnymatts",
                                        mutableListOf(),
                                                mutableListOf(ClassInfo("Foo", "com.jonnymatts"))
                                )
                        ),
                        mutableListOf()
                )
        )

        val pippoResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("package", "com.jonnymatts")
                .get("/randomClass")

        pippoResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("package", equalTo("com.jonnymatts"))
                .body("name", isOneOf("Foo"))
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