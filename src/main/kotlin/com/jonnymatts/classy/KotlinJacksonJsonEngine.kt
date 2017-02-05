package com.jonnymatts.classy

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ro.pippo.jackson.JacksonJsonEngine

class KotlinJacksonJsonEngine() : JacksonJsonEngine() {

    override fun getObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }
}