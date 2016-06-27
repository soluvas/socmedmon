package org.soluvas.socmedmon.mvc;

import com.google.common.collect.ImmutableMap;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by ceefour on 28/06/2016.
 */
@RestController
@RequestMapping("api")
public class ApiController {

    @Inject
    private Environment env;

    @RequestMapping(value = "config.json", method = RequestMethod.GET, produces = "application/json")
    public Map<String, String> config() {
        return ImmutableMap.of("apiUri", env.getRequiredProperty("api.uri"));
    }
}
