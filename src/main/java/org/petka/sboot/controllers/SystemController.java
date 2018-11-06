package org.petka.sboot.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Value("${git.commit.message.short}")
    private String commitMessage;

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;

    @Value("${git.build.time}")
    private String buildTime;

    @Value("${git.build.version}")
    private String buildVersion;

    @Value("${git.commit.time}")
    private String commitTime;

    @Value("${git.commit.user.email}")
    private String userEmail;

    @Value("${git.tags}")
    private String tags;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String version;


    @RequestMapping(value = "/versionInfo", method = RequestMethod.POST)
    public Map<String, String> getVersionInfo() {
        Map<String, String> result = new TreeMap<>();
        result.put("Application name", applicationName);
        result.put("Commit message", commitMessage);
        result.put("Commit branch", branch);
        result.put("Commit id", commitId);
        result.put("Commit time", commitTime);
        result.put("Build time", buildTime);
        result.put("Build version", buildVersion);
        result.put("User email", userEmail);
        result.put("Tag", tags);
        result.put("Version", version);
        return result;
    }

}
