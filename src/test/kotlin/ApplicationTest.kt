package example.omnidian.jbr

import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.context.WebApplicationContext
import kotlin.test.Test

@SpringBootTest
class ApplicationTest {
  @Autowired
  private var wac: WebApplicationContext? = null

  @Test
  fun failureCase() {
    val injectables = wac!!.getBeansOfType(IInjectable::class.java).values
    val mapper: ObjectMapper = wac!!.getBean("dangerous", ObjectMapper::class.java)
        .setInjectableValues(InjectableValues.Std().also { injectableValues ->
          injectables.forEach { injectableValues.addValue(it.javaClass, it) }
        })

    val targetSource = """{
    "@class": "example.omnidian.jbr.DangerousTarget",
    "data": "538520b1-3aff-11ee-a18d-125e2f51a24d"
    }"""

    assertDoesNotThrow { (mapper.readValue(targetSource, ITarget::class.java)) }
  }

  @Test
  fun successCase() {
    val injectables = wac!!.getBeansOfType(IInjectable::class.java).values
    val mapper: ObjectMapper = wac!!.getBean("safe", ObjectMapper::class.java)
        .setInjectableValues(InjectableValues.Std().also { injectableValues ->
          injectables.forEach { injectableValues.addValue(it.javaClass, it) }
        })

    val targetSource = """{
    "@class": "example.omnidian.jbr.SafeTarget",
    "data": "538520b1-3aff-11ee-a18d-125e2f51a24d"
    }"""

    assertDoesNotThrow { (mapper.readValue(targetSource, ITarget::class.java)) }
  }

}