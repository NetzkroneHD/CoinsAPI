package de.netzkronehd.coins.dependency.impl;


import de.netzkronehd.coins.dependency.Dependency;
import de.netzkronehd.coins.dependency.exception.DependencyDownloadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.net.http.HttpResponse.BodyHandlers.ofByteArray;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DependencyManagerImplTest {

    @TempDir
    private Path downloadTestPath;

    @Test
    void when_downloadDependency_with_valid_checksum_should_saveFile() throws Exception {
        final var dependencyManager = new DependencyManagerImpl(downloadTestPath);
        final var downloadedPath = dependencyManager.downloadDependency(Dependency.POSTGRESQL);

        assertTrue(Files.exists(downloadedPath));
    }

    @Test
    void when_downloadDependency_with_invalid_checksum_should_throwException() throws Exception {
        final var downloadedFileBytes = Files.readAllBytes(Path.of("src/test/resources/dependency-manager-test/invalid-checksum-file.txt"));
        final HttpResponse<byte[]> response = (HttpResponse<byte[]>) mock(HttpResponse.class);

        try (final var mockedStaticHttpClient = mockStatic(HttpClient.class)) {
            final var httpClient = mock(HttpClient.class);
            mockedStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);
            when(response.statusCode()).thenReturn(200);
            when(response.body()).thenReturn(downloadedFileBytes);
            when(httpClient.send(any(HttpRequest.class), any(ofByteArray().getClass()))).thenReturn(response);

            final var dependencyManager = new DependencyManagerImpl(Path.of("target"));
            mockedStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);
            assertThrows(DependencyDownloadException.class, () -> dependencyManager.downloadDependency(Dependency.POSTGRESQL));

        }
    }

}
