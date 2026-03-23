package de.netzkronehd.coins.dependency.exception;

import de.netzkronehd.coins.dependency.Dependency;

import java.util.Base64;

public class DependencyChecksumMismatchException extends DependencyDownloadException {

    public DependencyChecksumMismatchException(Dependency dependency, byte[] checksum) {
        super("The checksum of the dependency " + dependency + " does not match the expected checksum. Expected: '" + Base64.getEncoder().encodeToString(dependency.getChecksum()) + "' Got: '" + Base64.getEncoder().encodeToString(checksum) + "'");
    }

}
