package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.IntervalRule;

import java.util.List;

@Data
public class RuleSet {
    private List<IntegerRule> integerRules;
    private List<StringRule> stringRules;
    private List<FloatRule> floatRules;
    private List<DateRule> dateRules;
    private List<BooleanRule> booleanRules;
    private List<IntervalRule> intervalRules;
}
