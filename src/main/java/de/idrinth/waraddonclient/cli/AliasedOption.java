package de.idrinth.waraddonclient.cli;

import org.apache.commons.cli.Option;

public class AliasedOption extends Option {
    private final String original;

    public AliasedOption(String longOpt, boolean required, String description, String originalLongOpt) {
        super(null, longOpt, required, description);
        original = originalLongOpt;
    }

    public String getOriginal()
    {
        return original;
    }
}
