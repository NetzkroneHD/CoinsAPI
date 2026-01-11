package de.netzkronehd.coins.locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationService {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final Map<Locale, Set<String>> installed;
    private final Key key;
    private final MiniMessageTranslationStore store;

    public TranslationService(Key key) {
        this.installed = new ConcurrentHashMap<>();
        this.key = key;
        this.store = MiniMessageTranslationStore.create(key);
        this.store.defaultLocale(DEFAULT_LOCALE);
    }

    public boolean registerStoreInGlobalTranslator() {
        GlobalTranslator.translator().removeSource(store);
        return GlobalTranslator.translator().addSource(store);
    }

    public void loadFromFileSystem(Path directory) throws IOException, UnknownLocaleException {
        loadFromFileSystem(directory, (path) -> {});
    }

    public void loadFromFileSystem(Path directory, Consumer<Path> fileCallback) throws IOException, UnknownLocaleException {
        final List<Path> translationFiles;

        try (Stream<Path> stream = Files.list(directory)) {
            translationFiles = stream.filter(TranslationService::isTranslationFile).toList();
        }

        final Map<Locale, ResourceBundle> loaded = new HashMap<>();
        for (Path translationFile : translationFiles) {
            fileCallback.accept(translationFile);
            final Map.Entry<Locale, ResourceBundle> result = loadTranslationFile(translationFile);
            loaded.put(result.getKey(), result.getValue());
        }
        loaded.forEach((locale, bundle) -> {
            final Locale localeWithoutCountry = Locale.of(locale.getLanguage());
            if (!locale.equals(localeWithoutCountry) && !localeWithoutCountry.equals(DEFAULT_LOCALE) && !this.installed.containsKey(localeWithoutCountry)) {
                this.store.registerAll(localeWithoutCountry, bundle, false);
            }
        });
        registerStoreInGlobalTranslator();
    }

    public Map.Entry<Locale, ResourceBundle> loadTranslationFile(Path translationFile) throws IOException, UnknownLocaleException {
        final String fileName = translationFile.getFileName().toString();
        final String localeString = fileName.substring(0, fileName.length() - ".properties".length());
        final Locale locale = parseLocale(localeString)
                .orElseThrow(() -> new UnknownLocaleException("Unknown locale '" + localeString + "' - unable to register."));

        final PropertyResourceBundle bundle;
        try (BufferedReader reader = Files.newBufferedReader(translationFile, StandardCharsets.UTF_8)) {
            bundle = new PropertyResourceBundle(reader);
        }

        this.store.registerAll(locale, bundle, false);
        this.installed.put(locale, bundle.keySet());
        return new AbstractMap.SimpleImmutableEntry<>(locale, bundle);
    }

    public boolean unregister(String key) {
        for (Set<String> keys : this.installed.values()) {
            if (keys.remove(key)) {
                this.store.unregister(key);
                return true;
            }
        }
        return false;
    }

    public void unregisterAll() {
        this.installed.values().stream()
                .flatMap(Collection::stream)
                .forEach(this.store::unregister);
        this.installed.clear();
    }

    public Set<String> getAllKeys() {
        return installed.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Map<Locale, Set<String>> getInstalled() {
        return Collections.unmodifiableMap(installed);
    }

    public Key getKey() {
        return key;
    }

    public MiniMessageTranslationStore getStore() {
        return store;
    }

    public static Component render(Component component, @Nullable Locale locale) {
        return GlobalTranslator.render(component, (locale == null ? DEFAULT_LOCALE:locale));
    }

    public static Optional<Locale> parseLocale(@Nullable String locale) {
        if(!isValidLocaleString(locale)) {
            return Optional.empty();
        }
        return Optional.ofNullable(Translator.parseLocale(locale));
    }

    public static boolean isTranslationFile(Path path) {
        return path.getFileName().toString().endsWith(".properties");
    }

    private static boolean isValidLocaleString(String locale) {
        return locale != null && !locale.isBlank();
    }

}
