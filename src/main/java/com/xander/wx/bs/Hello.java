package com.xander.wx.bs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author Xander
 * datetime: 2019/8/22 17:16
 */
@RestController
@RequestMapping(value = "/hello")
public class Hello {

    @RequestMapping(value = "")
    public String hello() {
        return "hello xdWx!";
    }
}
