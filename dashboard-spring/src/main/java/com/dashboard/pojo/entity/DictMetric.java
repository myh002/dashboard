package com.dashboard.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_metric")
public class DictMetric {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer categoryId;
    private String metricName;
    private String unit;
    private Integer sortOrder;
}
