package com.exozet.gitresumeweb.web.view;

import com.exozet.gitresumeweb.web.view.AbstractBaseView;
import com.google.common.collect.Sets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.util.StringUtils;

/**
 * represents mvc model object calculating ratios of containing data entries by data key given
 * @param <T>
 */
@Data
public class RatioView<T> extends AbstractBaseView {
    Logger logger = LoggerFactory.getLogger(getClass());

    @ModelAttribute("entries")
    public Set<RatioViewEntry> getEntries() {
        return entries;
    }

    private Set<RatioViewEntry> entries = Sets.newHashSet();

    public RatioView(List<T> data, Class<T> dataType, String dataKey) throws NoSuchMethodException {
        Method getAccessor = dataType.getDeclaredMethod("get"+ StringUtils.capitalize(dataKey));
       
        data.stream().filter(e -> {
            try {
                return getAccessor.invoke(e) != null;
            } catch (InvocationTargetException | IllegalAccessException e1) {
                logger.trace(e1.getMessage());
            }

            return false;
        }).forEach(e -> {
            try {
                entries.add(updateRatio(getAccessor.invoke(e).toString(), data.size()));
            } catch (InvocationTargetException | IllegalAccessException e1) {
                logger.trace(e1.getMessage());
            }
        });

    }

    private RatioViewEntry updateRatio(String dataValue, Integer size){

        RatioViewEntry match =
                entries.stream().filter(e1 -> e1.modelKey != null && e1.modelKey.equals(dataValue)).findFirst().orElse(new RatioViewEntry());

        match.count++;

        match.modelKey = dataValue;

        match.modelValue = new Float(match.count) / size * 100;

        return match;
    }

    public static class RatioViewEntry{
        Integer count = 0;
        String modelKey;
        Float modelValue = 0f;

        @ModelAttribute("name")
        public String getName(){
            return modelKey;
        }

        @ModelAttribute("value")
        public Float getValue(){
            return modelValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RatioViewEntry)) {
                return false;
            }
            RatioViewEntry that = (RatioViewEntry) o;
            return Objects.equals(modelKey, that.modelKey);
        }

        @Override
        public int hashCode() {

            return Objects.hash(modelKey);
        }
    }

}
