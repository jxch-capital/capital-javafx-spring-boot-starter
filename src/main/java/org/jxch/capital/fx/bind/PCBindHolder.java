package org.jxch.capital.fx.bind;

import javafx.scene.Parent;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PCBindHolder<T> {
    private Parent parent;
    private T controller;
}
