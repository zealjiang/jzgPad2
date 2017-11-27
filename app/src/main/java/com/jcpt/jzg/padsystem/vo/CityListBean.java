package com.jcpt.jzg.padsystem.vo;

import java.util.ArrayList;

/**
 * Created by zealjiang on 2017/2/13 18:38.
 * Email: zealjiang@126.com
 */

public class CityListBean {

    private ArrayList<CityList> cityListBean;

    public ArrayList<CityList> getCityListBean() {
        return cityListBean;
    }

    public void setCityListBean(ArrayList<CityList> cityListBean) {
        this.cityListBean = cityListBean;
    }

    public class CityList{
        private City city;
        private ArrayList<City> cityList;

        public City getCity() {
            return city;
        }
        public void setCity(City city) {
            this.city = city;
        }
        public ArrayList<City> getCityList() {
            return cityList;
        }
        public void setCityList(ArrayList<City> cityList) {
            this.cityList = cityList;
        }

        public class City{
            private int cityId;
            private String cityName;
            private int parentId;

            public int getCityId() {
                return cityId;
            }
            public void setCityId(int cityId) {
                this.cityId = cityId;
            }
            public String getCityName() {
                return cityName;
            }
            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
            public int getParentId() {
                return parentId;
            }
            public void setParentId(int parentId) {
                this.parentId = parentId;
            }
        }
    }
}
