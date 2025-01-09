package com.example.crypticlibexample;

import crypticlib.config.ConfigHandler;
import crypticlib.config.node.impl.bukkit.StringConfig;
import crypticlib.config.node.impl.bukkit.StringListConfig;

import java.util.Arrays;
import java.util.List;

@ConfigHandler(path = "example.yml")
public class ExampleConfig {

    public static final StringConfig test = new StringConfig("test", "test", Arrays.asList("test 1"));
    public static final StringListConfig test2 = new StringListConfig("test2", List.of("1", "2", "3"), Arrays.asList("test 2", "A list config node"));

}
