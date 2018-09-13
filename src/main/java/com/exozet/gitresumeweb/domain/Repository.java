package com.exozet.gitresumeweb.domain;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.kohsuke.github.GHRepository;

public final class Repository {

    public static Instant calculateProjectAverageDuration(List<GHRepository> repositories) {
        List<Instant> durations =
                repositories.stream().map(e -> {
                    try {
                        return Instant.ofEpochMilli(e.getUpdatedAt().getTime() - e.getCreatedAt().getTime());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    return Instant.ofEpochMilli(0);
                }).collect(Collectors.toList());

        return Instant.ofEpochMilli(durations.stream().collect(Collectors.averagingLong(e2 -> e2.getEpochSecond())).longValue());
    }
}
