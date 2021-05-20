package de.idrinth.waraddonclient.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.cli.Option;

public class AliasOption extends Option {
    private final List<String> alternatives = new ArrayList<>();

    public AliasOption(String opt, String longOpt, boolean required, String description, String ...alternativeLongOpts) {
        super(opt, longOpt, required, description);
        alternatives.addAll(Arrays.asList(alternativeLongOpts));
    }

    public List<String> getAliases()
    {
        return Collections.unmodifiableList(alternatives);
    }
}
