-- ============================================
-- 高校部门统计数据 - 数据库结构文件 (schema.sql)
-- 方案二：多表规范化设计
-- 3 张表：dict_category + dict_metric + dept_stats
-- 包含 10 个部门，2020-2025 年数据
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 删除已有表（按外键依赖倒序）
DROP TABLE IF EXISTS `dept_stats`;
DROP TABLE IF EXISTS `dict_metric`;
DROP TABLE IF EXISTS `dict_category`;

-- ============================================
-- 1. 指标分类字典表
-- ============================================
CREATE TABLE `dict_category` (
    `id`              INT             NOT NULL AUTO_INCREMENT  COMMENT '分类ID',
    `dept_name`       VARCHAR(100)    NOT NULL                 COMMENT '所属部门',
    `category_name`   VARCHAR(100)    NOT NULL                 COMMENT '分类名称（项目列值，无项目时为"综合数据"）',
    `description`     VARCHAR(200)    DEFAULT NULL             COMMENT '分类描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_category` (`dept_name`, `category_name`),
    INDEX `idx_dept_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标分类字典表';

-- ============================================
-- 2. 指标字典表
-- ============================================
CREATE TABLE `dict_metric` (
    `id`              INT             NOT NULL AUTO_INCREMENT  COMMENT '指标ID',
    `category_id`     INT             NOT NULL                 COMMENT '所属分类ID',
    `metric_name`     VARCHAR(200)    NOT NULL                 COMMENT '指标名称',
    `unit`            VARCHAR(20)     DEFAULT '个'             COMMENT '单位',
    `sort_order`      INT             DEFAULT 0                COMMENT '排序',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_metric` (`category_id`, `metric_name`),
    INDEX `idx_category_id` (`category_id`),
    CONSTRAINT `fk_metric_category` FOREIGN KEY (`category_id`) REFERENCES `dict_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='指标字典表';

-- ============================================
-- 3. 部门统计数据表（单表，dept_name 区分部门）
-- ============================================
CREATE TABLE `dept_stats` (
    `id`              BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `dept_name`       VARCHAR(100)    NOT NULL                 COMMENT '部门名称',
    `metric_id`       INT             NOT NULL                 COMMENT '指标ID',
    `year`            INT                              COMMENT '统计年份',
    `value`           DECIMAL(15,2)   NOT NULL DEFAULT 0       COMMENT '指标数值',
    `created_at`      DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_metric_year` (`dept_name`, `metric_id`, `year`),
    INDEX `idx_dept_year` (`dept_name`, `year`),
    INDEX `idx_metric_id` (`metric_id`),
    CONSTRAINT `fk_stats_metric` FOREIGN KEY (`metric_id`) REFERENCES `dict_metric` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门统计数据表';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 4. 常用查询示例
-- ============================================

-- 查询某部门某年所有指标（带分类名）
SELECT
    c.category_name,
    m.metric_name,
    m.unit,
    s.year,
    s.value
FROM dept_stats s
JOIN dict_metric m ON s.metric_id = m.id
JOIN dict_category c ON m.category_id = c.id
WHERE s.dept_name = '科技处' AND s.year = 2025
ORDER BY c.category_name, m.sort_order;

-- 查询某指标历年趋势
SELECT
    s.dept_name,
    m.metric_name,
    s.year,
    s.value
FROM dept_stats s
JOIN dict_metric m ON s.metric_id = m.id
WHERE m.metric_name = '国家级项目数'
ORDER BY s.year;

-- 跨部门对比（直接 WHERE，无需 UNION ALL）
SELECT
    s.dept_name,
    c.category_name,
    m.metric_name,
    s.year,
    s.value
FROM dept_stats s
JOIN dict_metric m ON s.metric_id = m.id
JOIN dict_category c ON m.category_id = c.id
WHERE s.dept_name IN ('科技处', '人事处', '教务处')
  AND s.year = 2025
ORDER BY s.dept_name, c.category_name, m.sort_order;

-- 按部门汇总指标数量
SELECT
    s.dept_name,
    COUNT(DISTINCT m.id) as metric_count,
    COUNT(s.id) as data_count
FROM dept_stats s
JOIN dict_metric m ON s.metric_id = m.id
GROUP BY s.dept_name
ORDER BY s.dept_name;

-- 同比增长计算
SELECT
    s.dept_name,
    m.metric_name,
    curr.year,
    curr.value as current_value,
    prev.value as prev_value,
    CASE WHEN prev.value > 0 THEN
        ROUND((curr.value - prev.value) / prev.value * 100, 2)
    ELSE NULL END as yoy_growth_pct
FROM dept_stats curr
JOIN dept_stats prev
  ON curr.dept_name = prev.dept_name
 AND curr.metric_id = prev.metric_id
 AND curr.year = prev.year + 1
JOIN dict_metric m ON curr.metric_id = m.id
WHERE curr.dept_name = '科技处'
ORDER BY m.metric_name, curr.year;
