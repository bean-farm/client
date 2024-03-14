package com.beanfarm.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.beanfarm.model.ActionResponse;
import com.beanfarm.model.BeanfarmResponse;
import com.beanfarm.model.ProfilePost;
import com.beanfarm.model.ProfileResponse;

import ch.qos.logback.core.joran.action.Action;
import java.util.List;
public interface BeanfarmClient {

    @GetExchange("api/employees")
    BeanfarmResponse random(@RequestHeader(name = "access_token") String token);

    @GetExchange("api/getaction")
    List<ActionResponse> actions(@RequestHeader(name = "access_token") String token);

    @GetExchange("api/getplotsbyid")
    List<String> getUsersPlots(@RequestHeader(name = "access_token") String token);

    @GetExchange("api/listbeans")
    List<String> listBeans(@RequestHeader(name = "access_token") String token);

    @PostExchange("api/createprofile")
    ProfileResponse createProfile(@RequestHeader(name = "access_token") String token, @RequestBody ProfilePost profile);
    
    @PostExchange("api/plantbean/{beanPlotId}/{beanTypeID}")
    List<String> plantBean(@RequestHeader(name = "access_token") String token, @PathVariable(value = "beanPlotId") Long plotId, @PathVariable(value = "beanTypeID") Long beanTypeId);
    
    @PutExchange("api/waterplot/{beanPlotId}")
    List<String> waterPlot(@RequestHeader(name = "access_token") String token, @PathVariable(value = "beanPlotId") Long plotId);

    @PutExchange("api/harvestbeans/{beanPlotId}")
    List<String> harvestPlot(@RequestHeader(name = "access_token") String token, @PathVariable(value = "beanPlotId") Long plotId);

}
