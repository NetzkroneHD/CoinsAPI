package de.netzkronehd.coins.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
@Builder
public class CoinsConfig {

    private DecimalFormat decimalFormat;
    private String currencySymbol;
    private String currencyNamePlural;
    private String currencyNameSingular;


}
