package com.bdpqchen.yellowpagesmodule.yellowpages.model;

import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataBean {


    private List<CategoryListBean> category_list;

    public List<CategoryListBean> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(List<CategoryListBean> category_list) {
        this.category_list = category_list;
    }

    public static class CategoryListBean {
        /**
         * category_name : 校级
         * department_list : [{"department_name":"学工部","unit_list":[{"item_name":"学工部部长室","item_phone":"27403289"},{"item_name":"副部长","item_phone":"27403289"}]},{"department_name":"宣传部","unit_list":[{"item_name":"宣传部校报","item_phone":"string"},{"item_name":"宣传部广播站","item_phone":"27407083"}]}]
         */

        private String category_name;
        private List<DepartmentListBean> department_list;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<DepartmentListBean> getDepartment_list() {
            return department_list;
        }

        public void setDepartment_list(List<DepartmentListBean> department_list) {
            this.department_list = department_list;
        }

        public static class DepartmentListBean {
            /**
             * department_name : 学工部
             * unit_list : [{"item_name":"学工部部长室","item_phone":"27403289"},{"item_name":"副部长","item_phone":"27403289"}]
             */

            private String department_name;
            private List<UnitListBean> unit_list;

            public String getDepartment_name() {
                return department_name;
            }

            public void setDepartment_name(String department_name) {
                this.department_name = department_name;
            }

            public List<UnitListBean> getUnit_list() {
                return unit_list;
            }

            public void setUnit_list(List<UnitListBean> unit_list) {
                this.unit_list = unit_list;
            }

            public static class UnitListBean {
                /**
                 * item_name : 学工部部长室
                 * item_phone : 27403289
                 */

                private String item_name;
                private String item_phone;

                public String getItem_name() {
                    return item_name;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }

                public String getItem_phone() {
                    return item_phone;
                }

                public void setItem_phone(String item_phone) {
                    this.item_phone = item_phone;
                }
            }
        }
    }
}
