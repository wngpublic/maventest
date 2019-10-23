package org.wayne.db;

import org.wayne.io.MyLogger;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MySQLClasses implements MyBasic {

    MyLogger logger = MyLogger.instance;
    @Override
    public void shutdown() {
        logger.shutdown();
    }

    @Override
    public void init() {
        logger.testSetDebug();
    }

    public MySQLClasses() {
        init();
    }

    static class Entity {
        static int ctr = 0;
        public int id;              // id is globally unique, just a running ctr
        public Integer govid;       // unique per country
        public String namefirst;
        public String namelast;
        public String phone;
        public String email;
        public Integer addressid;   // unique per country
        public int countrycode;
        public long datestart;
        public Long dateend;
        public int entityType;
        public String gender;
        public Entity(
            Integer govid,
            int entityType,
            String gender,
            String namefirst,
            String namelast,
            String phone,
            String email,
            Integer addressid,
            int countrycode,
            long datestart,
            Long dateend
        )
        {
            this.id = ctr++;
            this.govid = (govid == null) ? id : govid;
            this.entityType = entityType;
            this.gender = gender;
            this.namefirst = namefirst;
            this.namelast = namelast;
            this.phone = phone;
            this.email = email;
            this.addressid = addressid;
            this.countrycode = countrycode;
            this.datestart = datestart;
            this.dateend = dateend;
        }
    }
    static class Person extends Entity {
        List<Person> contacts = new ArrayList<>();
        List<Company> historyEmployment = new ArrayList<>();
        List<Entity> historyEducation = new ArrayList<>();
        List<Address> historyMigration = new ArrayList<>();
        public Person(
            Integer govid,
            int entityType,
            String gender,
            String namefirst,
            String namelast,
            String phone,
            String email,
            Integer addressid,
            int countrycode,
            long datestart,
            Long dateend
        )
        {
            super(govid,entityType,gender,namefirst,namelast,phone,email,addressid,countrycode,datestart,dateend);
        }
    }
    static class Employee {
        static int ctr = 0;
        public int id;
        public int companyid;
        public int employeeid;
        public int entityid;
        public int countryid;
        public String email;
        public Employee employer = null;
        public List<Employee> employees = null;
        public Employee(
            int companyid,
            int employeeid,
            int entityid,
            int countryid,
            String email
        ) {
            this.id = ctr++;
            this.companyid = companyid;
            this.employeeid = employeeid;
            this.entityid = entityid;
            this.countryid = countryid;
            this.email = email;
        }
        public void setEmployer(Employee employee) {
            this.employer = employee;
        }
        public void addEmployee(Employee employee) {
            if(employees == null) {
                employees = new ArrayList<>();
            }
            employee.setEmployer(this);
            employees.add(employee);
        }
        public Employee getEmployer() {
            return employer;
        }
        public List<Employee> getEmployees() {
            return employees;
        }
    }
    static class Company extends Entity {
        static int ctr = 0;
        AtomicInteger uid = new AtomicInteger(0);
        private Utils utils = new Utils();
        public String name;
        public String domainname;
        public int companySize = 0;
        private List<Employee> employees = new ArrayList<>();
        private Departments departments = null;
        private int csuiteSize = 0;
        public Company(
            int govid,                  // country specific id, not globally unique
            int entityType,
            String name,
            int addressid,
            String phone,
            String domainname,
            long datestart,
            int idowner,                // globally unique id of entity
            int countryid
        )
        {
            super(govid, entityType, null, null, name, phone, "admin@"+domainname, addressid, countryid, datestart, null);
            this.name = name;
            this.addressid = addressid;
            this.domainname = domainname;
            csuiteSize = utils.getInt(3,10);
        }
        int getNextReceiptId() {
            return uid.getAndIncrement();
        }
        public Employee getHead() {
            return departments.get().get(0).getHead();
        }
        private boolean allDeptPopulated = false;
        public boolean allDeptPopulated() {
            return allDeptPopulated;
        }
        public boolean addEmployee(Employee employee) {
            employees.add(employee);

            Department departmentCSuite = departments.get().get(0);

            // populate CSUITE
            if(departmentCSuite.getEmployees().size() < csuiteSize) {
                Employee head = departmentCSuite.getHead();
                if(head == null) {
                    departmentCSuite.addEmployee(null, employee);
                    return true;
                }
                departmentCSuite.addEmployee(head, employee);
                return true;
            }

            // populate HEADS, do not populate csuite
            for(Department department: departments.get()) {
                if(department.getHead() == null) {
                    List<Employee> listExecs = departmentCSuite.getEmployees();
                    Employee manager = listExecs.get(utils.getInt(1,listExecs.size()-1));
                    department.addEmployee(manager, employee);
                    return true;
                }
            }
            allDeptPopulated = true;
            // populate randomly, do not populate csuite
            List<Department> list = departments.get();
            Department department = list.get(utils.getInt(1, list.size()-1));
            addEmployeeToEmployer(department.getHead(), employee, department);
            return true;
        }
        public List<Employee> getEmployees() {
            return employees;
        }
        public void setCompanySize(int companySize) {
            this.companySize = companySize;
        }
        public int getCompanySize() {
            return companySize;
        }
        public void setDepartments(Departments departments) {
            this.departments = departments;
        }
        public Departments getDepartments() {
            return departments;
        }
        private void addEmployeeToEmployer(Employee employer, Employee employee, Department department) {
            // 20% of time add to existing employee
            if(employer.getEmployees() == null || employer.getEmployees().size() <= 8) {
                department.addEmployee(employer, employee);
                employer.addEmployee(employee);
                return;
            }
            List<Employee> listEmployees = employer.getEmployees();
            int idx = utils.getInt(0,listEmployees.size()-1);
            Employee manager = listEmployees.get(idx);
            addEmployeeToEmployer(manager, employee, department);
        }

    }
    static class Department {
        static int ctr = 0;
        public int id;
        public int companyid;
        public int deptid;
        public String deptname;
        public String deptcategory;
        public Department(
            int companyid,
            int deptid,
            String deptname,
            String deptcategory) {
            this.id = ctr++;
            this.companyid = companyid;
            this.deptid = deptid;
            this.deptname = deptname;
            this.deptcategory = deptcategory;
        }
        private Employee head = null;
        private List<Employee> list = new ArrayList<>();
        private Departments departments = null;
        public Employee getHead() {
            return head;
        }
        public boolean isExecutive() {
            return deptid == 1;
        }
        public List<Employee> getEmployees() { return list; }
        public void addEmployee(Employee employer, Employee employee) {
            list.add(employee);
            if(head == null)
                head = employee;
            if(employer == null)
                return;
            employer.addEmployee(employee);
        }
        public void addDepartment(Department department) {
            if(departments == null)
                departments = new Departments();
            departments.add(department);
        }
        public Departments getDepartments() {
            return departments;
        }
    }
    static class Countries {
        List<Country> set = new ArrayList<>();
        void add(Country entity) {
            set.add(entity);
        }
        Collection<Country> get() {
            return set;
        }
    }
    static class Addresses {
        Map<Country, List<Address>> map = new HashMap<>();
        void add(Country country, Address entity) {
            if(map.get(country) == null) {
                map.put(country, new ArrayList<>());
            }
            List<Address> set = map.get(country);
            set.add(entity);
        }
        Collection<Address> get(Country country) {
            return map.get(country);
        }
        Collection<Country> getCountries() {
            return map.keySet();
        }
    }
    static class Address {
        public static int ctrgid = 0;
        public int gid = 0;
        public static int ctr = 0;
        public int id;
        public Integer govid;
        public Integer unit;
        public int addr_num;
        public int street_id;
        public int city_id;
        public int province_id;
        public int country_id;
        public String zip;
        public Address(
            Integer govid,
            Integer unit,
            int addr_num,
            int streetid,
            int cityid,
            int provinceid,
            int countryid,
            String zip
        ) {
            this.gid = ctrgid++;
            this.id = govid;
            this.govid = govid;
            this.unit = unit;
            this.addr_num = addr_num;
            this.street_id = streetid;
            this.city_id = cityid;
            this.province_id = provinceid;
            this.country_id = countryid;
            this.zip = zip;
        }
    }
    static class Province {
        public static int ctrgid = 0;
        public int gid = 0;
        public static int ctr = 0;
        public int id = 0;
        public Province() {
            gid = ctrgid++;
            id = ctr++;
        }
        List<City> cities = new ArrayList<>();
        public Collection<City> get() { return cities; }
        public void add(City city) {
            cities.add(city);
        }
        public String name = null;
    }
    static class Provinces {
        ArrayList<Province> provinces = new ArrayList<>();
        public Collection<Province> get() { return provinces; }
        public void add(Province province) {
            provinces.add(province);
        }
    }
    static class City {
        public static int ctrgid = 0;
        public int gid = 0;
        public static int ctr = 0;
        public int id = 0;
        public City() {
            gid = ctrgid++;
            id = ctr++;
        }
        public String name = null;
        public Collection<Street> streets = new ArrayList<>();
    }
    static class Street {
        public static int ctrgid = 0;
        public int gid = 0;
        public static int ctr = 0;
        public int id = 0;
        public Street() {
            gid = ctrgid++;
            id = ctr++;
        }
        public Collection<Street> streets = new ArrayList<>();
        public List<Address> addresses = new ArrayList<>();
        public String name = null;
    }
    static class Country {
        public static int ctrgid = 0;
        public int gid = 0;
        public Provinces provinces = null;
        static int ctr = 0;
        public int id;
        public int prefixcode;
        public String name;
        public String abbrev;
        List<Address> addresses;
        public Country(
            int prefixcode,
            String name,
            String abbrev
        ) {
            gid = ctrgid++;
            this.id = ctr++;
            this.prefixcode = prefixcode;
            this.name = name;
            this.abbrev = abbrev;

            Address.ctr = 0;
            Street.ctr = 0;
            City.ctr = 0;
            Province.ctr = 0;
        }
        public void setProvinces(Provinces provinces) {
            this.provinces = provinces;
        }
        public Provinces getProvinces() { return provinces; }
        public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
        public List<Address> getAddresses() { return addresses; }
        final static String fields = "id,prefixcode,name,abbrev";
        public String toSQL() {
            String values = String.format("%d,%d,%s,%s",
                id,prefixcode,name,abbrev);
            return "insert into table_address ("+fields+") values ("+values+");";
        }
    }
    static class Item {
        static long ctr = 0;
        public long id;
        public String name;
        public String category;
        public String subcategory;
        public int srcid;
        public Item(
            String name,
            String category,
            String subcategory,
            int srcid
        ) {
            this.id = ctr++;
            this.name = name;
            this.category = category;
            this.subcategory = subcategory;
            this.srcid = srcid;
        }
        final static String fields = "id,name,category,subcategory,srcid";
        public String toSQL() {
            String values = String.format("%d,%d,%s,%s",
                id,name,category,subcategory,srcid);
            return "insert into table_address ("+fields+") values ("+values+");";
        }
    }
    static class Transaction {
        ConcurrentHashMap<Integer,AtomicLong> map = new ConcurrentHashMap<>();
        static long ctr = 0;
        public long id;
        public long companytransactionid;
        public long grouptransactionid; // a group of transactions with
        // single src -> multi dst or
        // multi src -> single dst
        public int srcid;
        public int itemid;
        public int dstid;
        public int qty;
        public int dstaddress;
        public int dstemail;
        public double price;
        public long date;
        public Transaction(
            long grouptransactionid,
            int srcid,
            int itemid,
            int dstid,
            int qty,
            int dstaddress,
            int dstemail,
            double price,
            long date
        ) {
            this.id = ctr++;
            // not thread safe
            if(map.get(srcid) == null) {
                map.put(srcid, new AtomicLong(0));
            }
            this.companytransactionid = map.get(srcid).getAndIncrement();
            this.grouptransactionid = grouptransactionid;
            this.srcid = srcid;
            this.itemid = itemid;
            this.dstid = dstid;
            this.qty = qty;
            this.dstaddress = dstaddress;
            this.dstemail = dstemail;
            this.price = price;
            this.date = date;
        }
    }
    static class Product {
        static int ctr = 0;
        static Map<Company, Integer> mapCtr = new HashMap<>();
        int id;
        int companyProductId;
        int version;
        int quality;
        int category;
        String description;
        Company manufacturer;
        int company_id;
        int company_country_id;
        int priceCost;
        int priceSale;
        Entity seller;
        int seller_id;
        int seller_country_id;
        public Product(
            int category,
            int version,
            int quality,
            String description,
            Company manufacturer,
            int priceCost,
            int priceSale,
            Entity seller)
        {
           this.id = ctr++;
           this.category = category;
           this.version = version;
           this.quality = quality;
           this.description = description;
           this.manufacturer = manufacturer;
           this.company_id = manufacturer.id;
           this.company_country_id = manufacturer.countrycode;
           this.priceCost = priceCost;
           this.priceSale = priceSale;
           this.seller = seller;
           this.seller_id = seller.govid;
           this.seller_country_id = seller.countrycode;
           Integer productId = mapCtr.get(manufacturer);
           if(productId == null) productId = 1;
           else productId += 1;
           mapCtr.put(manufacturer, productId);
           companyProductId = productId;
        }
    }
    static class Products {
        Map<Integer, Set<Product>> map = new HashMap<>();
        void add(Product product, int category) {
            Set<Product> set = map.get(category);
            if(set == null) {
                set = new HashSet<>();
                map.put(category,set);
            }
            set.add(product);
        }
        Set<Product> getProducts(int category) {
            Set<Product> set = map.get(category);
            if(set == null) {
                return Collections.EMPTY_SET;
            } else {
                return set;
            }
            /*
            if(map.containsKey(category))
                return Collections.unmodifiableSet(map.get(category));
            return Collections.EMPTY_SET;
            */
        }
        List<Product> getProductsByCountries(int category, Set<Integer> countries) {
            if(!map.containsKey(category)) {
                return Collections.EMPTY_LIST;
            }
            Set<Product> setProducts = map.get(category);
            List<Product> listFiltered = new ArrayList<>();
            for(Product product: setProducts) {
                if(countries.contains(product.company_country_id)) {
                    listFiltered.add(product);
                }
            }
            return listFiltered;
        }
        // topNum 0 = means only top, topNum 1 means top 2
        List<Product> getProductsByQuality(int category, int topNum) {
            if(!map.containsKey(category)) {
                return Collections.EMPTY_LIST;
            }
            Set<Product> setProducts = map.get(category);
            List<Product> listFiltered = new ArrayList<>();
            int max = 0;
            for(Product product: setProducts) {
                max = (product.quality > max) ? product.quality : max;
            }
            int min = max - topNum;
            for(Product product: setProducts) {
                if(product.quality >= min) {
                    listFiltered.add(product);
                }
            }
            return listFiltered;
        }
        // topNum 0 = means only top, topNum 1 means top 2
        List<Product> getProductsByCountriesAndQuality(int category, Set<Integer> countries, int topNum) {
            if(!map.containsKey(category)) {
                return Collections.EMPTY_LIST;
            }
            Set<Product> setProducts = map.get(category);
            List<Product> listFiltered = new ArrayList<>();
            int max = 0;
            for(Product product: setProducts) {
                max = (product.quality > max) ? product.quality : max;
            }
            int min = max - topNum;
            for(Product product: setProducts) {
                if(product.quality >= min && countries.contains(product.company_country_id)) {
                    listFiltered.add(product);
                }
            }
            return listFiltered;
        }
        Set<Integer> getCategories() {
            return Collections.unmodifiableSet(map.keySet());
        }
    }
    static class SalesRecords {
        Map<Entity, List<SalesRecord>> mapSales = new HashMap<>();
        Map<Entity, List<ReceiptsRecord>> mapReceipts = new HashMap<>();

        void add(Entity entity, SalesRecord record) {
            List<SalesRecord> l = mapSales.get(entity);
            if(l == null) {
                l = new ArrayList<>();
                mapSales.put(entity,l);
            }
            l.add(record);
        }

        void add(Entity entity, ReceiptsRecord record) {
            List<ReceiptsRecord> l = mapReceipts.get(entity);
            if(l == null) {
                l = new ArrayList<>();
                mapReceipts.put(entity,l);
            }
            l.add(record);
        }

        Map<Entity, List<SalesRecord>> getSalesRecords() {
            return Collections.unmodifiableMap(mapSales);
        }
        Map<Entity, List<ReceiptsRecord>> getSalesReceipts() {
            return Collections.unmodifiableMap(mapReceipts);
        }
        List<SalesRecord> getSalesRecords(Entity entity) {
            List<SalesRecord> l = mapSales.get(entity);
            if(l == null) return Collections.EMPTY_LIST;
            return Collections.unmodifiableList(l);
        }

        List<ReceiptsRecord> getReceiptsRecord(Entity entity) {
            List<ReceiptsRecord> l = mapReceipts.get(entity);
            if(l == null) return Collections.EMPTY_LIST;
            return Collections.unmodifiableList(l);
        }

        Set<Entity> getSalesKeySet() {
            return Collections.unmodifiableSet(mapSales.keySet());
        }

        Set<Entity> getReceiptsKeySet() {
            return Collections.unmodifiableSet(mapReceipts.keySet());
        }
    }
    static class SalesRecord {
        static long idCtr = 0;
        static long dateCtr = 0;

        static {
            try {
                dateCtr = DataGenerator.date2millis("2017/01/01 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Long date;
        long id;
        Integer salesRecordId;
        int fromCountryId;
        int fromCompanyId;
        int quantity;
        int pricePerItem;
        int priceTotal;
        Integer toEntityId;
        Integer toCountryId;
        int productId;
        int productCategoryId;
        int version;

        public SalesRecord(
            Long date,
            Integer salesRecordId,
            int fromCountryId,
            int fromCompanyId,
            int quantity,
            int pricePerItem,
            int priceTotal,
            Integer toEntityId,
            Integer toCountryId,
            int productId,
            int productCategoryId,
            int version
        )
        {
            this.id = idCtr++;
            this.salesRecordId = salesRecordId;
            this.date = date;
            this.fromCountryId = fromCountryId;
            this.fromCompanyId = fromCompanyId;
            this.quantity = quantity;
            this.pricePerItem = pricePerItem;
            this.priceTotal = priceTotal;
            this.toEntityId = toEntityId;
            this.toCountryId = toCountryId;
            this.productId = productId;
            this.productCategoryId = productCategoryId;
            this.version = version;
        }
    }
    static class ReceiptsRecord {
        static long idCtr = 0;
        static long dateCtr;

        static {
            try {
                dateCtr = DataGenerator.date2millis("2017/01/01 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Long date;
        long id;
        Integer salesRecordId;
        int fromCountryId;
        int fromCompanyId;
        int quantity;
        int pricePerItem;
        int priceTotal;
        Integer toEntityId;
        Integer toCountryId;
        int productId;
        int productCategoryId;

        public ReceiptsRecord(
            Long date,
            Integer salesRecordId,
            int fromCountryId,
            int fromCompanyId,
            int quantity,
            int pricePerItem,
            int priceTotal,
            Integer toEntityId,
            Integer toCountryId,
            int productId,
            int productCategoryId
        )
        {
            this.id = idCtr++;
            this.salesRecordId = salesRecordId;
            this.date = date;
            this.fromCountryId = fromCountryId;
            this.fromCompanyId = fromCompanyId;
            this.quantity = quantity;
            this.pricePerItem = pricePerItem;
            this.priceTotal = priceTotal;
            this.toEntityId = toEntityId;
            this.toCountryId = toCountryId;
            this.productId = productId;
            this.productCategoryId = productCategoryId;
        }
    }

    static class Inventories {

    }
    static class Inventory {

    }
    static class Banks {

    }
    static class Bank {

    }
    static class Entities {
        private Map<Country, ArrayList<Entity>> map = new HashMap<>();
        private Map<Country, ArrayList<Entity>> mapCompanies = new HashMap<>();
        private Map<Country, ArrayList<Entity>> mapPeople = new HashMap<>();
        public void add(Country country, Entity entity) {
            if(map.get(country) == null)
                map.put(country, new ArrayList<>());
            map.get(country).add(entity);
            if(entity.entityType == 1) {
                if(mapCompanies.get(country) == null)
                    mapCompanies.put(country, new ArrayList<>());
                mapCompanies.get(country).add(entity);
            }
            else if(entity.entityType == 2) {
                if(mapPeople.get(country) == null)
                    mapPeople.put(country, new ArrayList<>());
                mapPeople.get(country).add(entity);
            }
        }
        Collection<Entity> getCompanies(Country country) {
            return mapCompanies.get(country);
        }
        Collection<Entity> getPeople(Country country) {
            return mapPeople.get(country);
        }
        Collection<Entity> get(Country country) {
            return map.get(country);
        }
        Collection<Country> getCountries() {
            return map.keySet();
        }
    }
    static class Companies {
        Map<Country, List<Company>> map = new HashMap<>();
        void add(Country country, Company entity) {
            List<Company> set = map.get(country);
            if(set == null) {
                set = new ArrayList<>();
                map.put(country, set);
            }
            set.add(entity);
        }
        Map<Country, List<Company>> get() { return Collections.unmodifiableMap(map); }
        Collection<Company> get(Country country) {
            return map.get(country);
        }
    }
    static class Departments {
        List<Department> set = new ArrayList<>();
        void add(Department entity) {
            set.add(entity);
        }
        List<Department> get() {
            return set;
        }
    }
    static class Data2SQL {
        public void
        getSQLAddresses(Map<MyTab, List<String>> tables, Street street, City city, Province province, Country country) {
            String args = String.format("id,unit,addr_num,street_id,city_id,province_id,country_id,zip");
            for(Address address: street.addresses) {
                String vals = String.format("%d,%d,%d,%d,%d,%d,%d,\"%s\"",
                    address.id,
                    address.unit,
                    address.addr_num,
                    street.id,
                    city.id,
                    province.id,
                    country.id,
                    address.zip);
                String line = String.format("insert into addresses (%s) values (%s);", args, vals);
                tables.get(MyTab.ADDRESS).add(line);
            }
        }
        public void
        getSQLStreets(Map<MyTab, List<String>> tables, City city, Province province, Country country) {
            String args = String.format("id,name,city_id,province_id,country_id");
            for(Street street: city.streets) {
                String vals = String.format("%d,\"%s\",%d,%d,%d",
                    street.id,
                    street.name,
                    city.id,
                    province.id,
                    country.id);
                String line = String.format("insert into streets (%s) values (%s);", args, vals);
                tables.get(MyTab.STREET).add(line);

                getSQLAddresses(tables,street,city,province,country);
            }
        }
        public void
        getSQLCities(Map<MyTab, List<String>> tables, Province province, Country country) {
            String args = String.format("id,name,province_id,country_id");
            for(City city: province.get()) {
                String vals = String.format("%d,\"%s\",%d,%d",city.id,city.name,province.id,country.id);
                String line = String.format("insert into cities (%s) values (%s);", args, vals);
                tables.get(MyTab.CITY).add(line);

                getSQLStreets(tables,city,province,country);
            }
        }
        public void
        getSQLProvinces(Map<MyTab, List<String>> tables, Country country) {
            String args = String.format("id,name,country_id");
            for(Province province: country.provinces.get()) {
                String vals = String.format("%d,\"%s\",%d",province.id,province.name,country.id);
                String line = String.format("insert into provinces (%s) values (%s);", args, vals);
                tables.get(MyTab.PROVINCE).add(line);

                getSQLCities(tables,province,country);
            }
        }
        public void
        getSQLCountries(Map<MyTab,List<String>> tables, Countries countries) {
            String args = String.format("id,name,abbrev,prefix");
            for(Country country: countries.get()) {
                String vals = String.format("%d,\"%s\",\"%s\",%d",country.id,country.name,country.abbrev,country.prefixcode);
                String line = String.format("insert into countries (%s) values (%s);", args, vals);
                tables.get(MyTab.COUNTRY).add(line);

                getSQLProvinces(tables,country);
            }
        }
        public void
        getSQLEntities(Map<MyTab,List<String>> tables, Entities allEntities) {
            String args = String.format("govid,namefirst,namelast,phone,email,gender,address_id,country_id,datestart,dateend,entity_type");
            for(Country country: allEntities.getCountries()) {
                Collection<Entity> entities = allEntities.get(country);
                for(Entity entity: entities) {
                    String vals = String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%d,%d,%d",
                        entity.govid,
                        entity.namefirst,
                        entity.namelast,
                        entity.phone,
                        entity.email,
                        entity.gender,
                        entity.addressid,
                        entity.countrycode,
                        entity.datestart,
                        entity.dateend,
                        entity.entityType);
                    String line = String.format("insert into entities (%s) values (%s);", args, vals);
                    tables.get(MyTab.ENTITY).add(line);
                }
            }
        }
        public void
        getSQLCompanies(Map<MyTab,List<String>> tables, Companies companies) {
            String args = String.format("id,govid,name,address_id,domainname,country_id");
            for(Country country: companies.map.keySet()) {
                for(Company company: companies.get(country)) {
                    String vals = String.format("%d,%d,\"%s\",%d,\"%s\",%d",
                        company.id,
                        company.govid,
                        company.name,
                        company.addressid,
                        company.domainname,
                        company.countrycode);
                    String line = String.format("insert into companies (%s) values (%s);", args, vals);
                    tables.get(MyTab.COMPANY).add(line);
                    getSQLDepartments(tables,company);
                }
            }
        }
        public void
        getSQLDepartments(Map<MyTab,List<String>> tables, Company company) {
            String args = String.format("id,companyid,deptid,deptname,deptcategory");
            for(Department department: company.getDepartments().get()) {
                String vals = String.format("%d,%d,%d,\"%s\",\"%s\"",
                    department.id,
                    department.companyid,
                    department.deptid,
                    department.deptname,
                    department.deptcategory);
                String line = String.format("insert into departments (%s) values (%s);", args, vals);
                tables.get(MyTab.DEPARTMENT).add(line);
                getSQLEmployees(tables,department);
            }
        }
        public void
        getSQLEmployees(Map<MyTab,List<String>> tables, Department department) {
            String args = String.format("govid,employee_id,company_id,email,dept_id,country_id,employer_id");
            for(Employee employee: department.getEmployees()) {
                Integer employer_id = employee.employer == null ? null : employee.employer.employeeid;
                String vals = String.format("%d,%d,%d,\"%s\",%d,%d,%d",
                    employee.entityid,
                    employee.employeeid,
                    department.companyid,
                    employee.email,
                    department.deptid,
                    employee.countryid,
                    employer_id);
                String line = String.format("insert into employees (%s) values (%s);", args, vals);
                tables.get(MyTab.EMPLOYEE).add(line);
            }
        }
        public void
        getSQLProducts(Map<MyTab,List<String>> tables, Products products) {
            String args = String.format("sku,company_product_id,version,quality,category,description,manufacturer_id,country_id,price_cost,price_sale,seller_id,seller_country_id");
            for(Integer category: products.getCategories()) {
                for(Product p: products.getProducts(category)) {
                    String vals = String.format("%d,%d,%d,%d,%d,\"%s\",%d,%d,%d,%d,%d,%d",
                        p.id,
                        p.companyProductId,
                        p.version,
                        p.quality,
                        p.category,
                        p.description,
                        p.company_id,
                        p.company_country_id,
                        p.priceCost,
                        p.priceSale,
                        p.seller_id,
                        p.seller_country_id);
                    String line = String.format("insert into products (%s) values (%s);", args, vals);
                    tables.get(MyTab.PRODUCT).add(line);
                }
            }
        }
        public void
        getSQLSalesRecords(Map<MyTab,List<String>> tables, SalesRecords salesRecords) {
            {
                String args = String.format("date,id,sales_record_id,from_country_id,from_company_id,qty,price_per_item,price_total,to_entity_id,to_country_id,product_id,category_id,version");
                Map<Entity, List<SalesRecord>> map = salesRecords.getSalesRecords();
                for(Map.Entry<Entity, List<SalesRecord>> kv: map.entrySet()) {
                    for(SalesRecord record: kv.getValue()) {
                        String vals = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",
                            record.date,
                            record.id,
                            record.salesRecordId,
                            record.fromCountryId,
                            record.fromCompanyId,
                            record.quantity,
                            record.pricePerItem,
                            record.priceTotal,
                            record.toEntityId,
                            record.toCountryId,
                            record.productId,
                            record.productCategoryId,
                            record.version);
                        String line = String.format("insert into receipts_seller (%s) values (%s);", args, vals);
                        tables.get(MyTab.RECEIPTS_SELL).add(line);
                    }
                }
            }
            {
                String args = String.format("date,id,sales_record_id,from_country_id,from_company_id,qty,price_per_item,price_total,to_entity_id,to_country_id,product_id,category_id");
                Map<Entity, List<ReceiptsRecord>> map = salesRecords.getSalesReceipts();
                for(Map.Entry<Entity, List<ReceiptsRecord>> kv: map.entrySet()) {
                    for(ReceiptsRecord record: kv.getValue()) {
                        String vals = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",
                            record.date,
                            record.id,
                            record.salesRecordId,
                            record.fromCountryId,
                            record.fromCompanyId,
                            record.quantity,
                            record.pricePerItem,
                            record.priceTotal,
                            record.toEntityId,
                            record.toCountryId,
                            record.productId,
                            record.productCategoryId);
                        String line = String.format("insert into receipts_customer (%s) values (%s);", args, vals);
                        tables.get(MyTab.RECEIPTS_BUY).add(line);
                    }
                }
            }
        }
    }
    static class DataGenerator {
        Utils utils = new Utils();
        public static String CHARSET = "abcdefghijklmnopqrstuvwxyz";
        public static long getRandDate(String datebeg, String dateend) throws ParseException {
            Random rand = new Random();
            long msbeg = date2millis(datebeg);
            long msend = date2millis(dateend);
            long msdiff = msend - msbeg + 1;
            int msrand = rand.nextInt((int)msdiff);
            long msdate = msbeg + msrand;
            return msdate;
        }

        public static long date2millis(String dateString) throws ParseException {
            /*
             * date must be yyyy/mm/dd hh:mm:ss else return 0
             */
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateString);
            return date.getTime();
        }

        public static String millis2date(long ms) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date(ms);
            return simpleDateFormat.format(date);
        }

        public City genCity(AtomicInteger ctr, Province province,
            int densityMultiunits, String name, String zip, int numStreets, int maxAddr, Country country)
        {
            City city = new City();
            city.name = name;
            for(int k = 0; k < numStreets; k++) {
                Street street = new Street();
                city.streets.add(street);
                street.name = utils.getRandString(CHARSET, 3) + k;
                int numAddresses = utils.getInt(ProfileDG.MIN_ADDR_PER_STREET,maxAddr);
                int addrNumber = 10;
                for(int m = 0; m < numAddresses; m++, addrNumber += 10) {
                    boolean hasUnits = utils.getInt(0,100) < densityMultiunits;
                    if(hasUnits) {
                        int numUnits = utils.getInt(ProfileDG.MIN_UNITS_PER_ADDRESS,ProfileDG.MAX_UNITS_PER_ADDRESS);
                        for(int n = 0; n < numUnits; n++) {
                            Address address = new Address(
                                ctr.incrementAndGet(), n+1, addrNumber, street.id, city.id, province.id, country.id,zip);
                            street.addresses.add(address);
                        }
                    }
                    else {
                        Address address = new Address(
                            ctr.incrementAndGet(), null, addrNumber, street.id, city.id, province.id, country.id,zip);
                        street.addresses.add(address);
                    }
                }
            }
            return city;
        }
        public Countries genCountries() {
            Countries countries = new Countries();
            int numCountries = utils.getInt(ProfileDG.MIN_COUNTRIES,ProfileDG.MAX_COUNTRIES);
            for(int i = 0; i < numCountries; i++) {
                String name = "Country " + i;
                String abbr = String.format("C%02d", i);
                Country country = new Country(i, name, abbr);
                genProvinces(country);
                countries.add(country);
            }
            for(Country country: countries.get()) {
                List<Address> addresses = new ArrayList<>();
                for(Province province: country.getProvinces().get()) {
                    for(City city: province.get()) {
                        for(Street street: city.streets) {
                            for(Address address: street.addresses) {
                                addresses.add(address);
                            }
                        }
                    }
                }
                country.setAddresses(addresses);
            }
            return countries;
        }
        public Provinces genProvinces(Country country) {
            int zipInt = 1;

            AtomicInteger ctr = new AtomicInteger(0);
            Provinces provinces = new Provinces();
            country.setProvinces(provinces);
            int numProvinces = utils.getInt(ProfileDG.MIN_PROVINCE,ProfileDG.MAX_PROVINCE);
            for(int i = 0; i < numProvinces; i++) {
                Province province = new Province();
                provinces.add(province);
                province.name = utils.getRandString(CHARSET,4) + i;
                int numCities = utils.getInt(ProfileDG.MIN_CITY_PER_PROVINCE,ProfileDG.MAX_CITY_PER_PROVINCE);
                for(int j = 0; j < numCities; j++) {
                    double densityPopulation = utils.getInt(ProfileDG.MIN_DENSITY_POPULATION,ProfileDG.MAX_DENSITY_POPULATION)/100.0;
                    int densityMultinuits = densityPopulation < 0.5 ? utils.getInt(0,10) :
                        densityPopulation < 0.75 ? utils.getInt(0,30) :
                            utils.getInt(50,99);
                    String zip = String.format("%08d",zipInt++);
                    String name = utils.getRandString(CHARSET,3) + j;
                    int numStreets = utils.getInt(ProfileDG.MIN_STREET_PER_CITY,ProfileDG.MAX_STREET_PER_CITY);
                    City city = genCity(ctr, province, densityMultinuits, name, zip, numStreets, ProfileDG.MAX_ADDR_PER_STREET, country);
                    province.add(city);
                }
            }
            return provinces;
        }
        public Entities genEntities(Countries countries) throws ParseException {
            Entities entities = new Entities();
            for(Country country: countries.get()) {
                List<Address> addresses = country.getAddresses();
                int numAddresses = addresses.size();
                int numCompanies = utils.getInt(
                    (int)(numAddresses * ProfileDG.MIN_PCT_ENTITY_COMPANY),
                    (int)(numAddresses * ProfileDG.MAX_PCT_ENTITY_COMPANY));
                if(ProfileDG.FILL_ALL_DEPARTMENTS && numAddresses < (numCompanies*10)) {
                    throw new ParseException(String.format("country %s has %d addresses, must be more than 10 * %d",
                        country.name, numAddresses, numCompanies), 0);
                }
                genEntitiesPerCountry(entities, country, addresses, numCompanies);
            }
            return entities;
        }
        public void genEntitiesPerCountry(
            Entities entities,
            Country country,
            List<Address> addresses,
            int numCompanies)
            throws ParseException
        {
            int govid = 0;
            int phoneCtr = 0;
            int emailCtr = 0;
            int numAddresses = addresses.size();
            int numHouseholds = numAddresses - numCompanies;
            int cid = country.id;
            String cAbbr = country.abbrev;
            int ctrEntities = 0;
            for(int i = 0; i < numCompanies; i++) {
                String namefirst = null;
                String namelast = "company" + i;
                String phone = String.format("%05d%05d", country.id,phoneCtr++);
                String email = String.format("%s@%s",namelast,cAbbr);
                Integer addressid = addresses.get(i).id;
                Long datestart = getRandDate("1980/01/01 00:00:00", "2018/01/01 00:00:00");
                Long dateend = null;
                Entity entity = new Entity(govid++,1,null,namefirst,namelast,phone,email,addressid,cid,datestart,dateend);
                entities.add(country, entity);
            }
            for(int i = 0; i < numHouseholds; i++) {
                int numInThisHousehold = utils.getInt(ProfileDG.MIN_ENTITY_PER_ADDRESS,ProfileDG.MAX_ENTITY_PER_ADDRESS);
                for(int j = 0; j < numInThisHousehold; j++) {
                    String namefirst = utils.getRandString(CHARSET,3);
                    String namelast = utils.getRandString(CHARSET,4);
                    String phone = String.format("%05d%05d", country.id,phoneCtr++);
                    String email = String.format("%s.%s.%d@%s",namefirst,namelast,emailCtr++,cAbbr);
                    String gender;
                    int randInt = utils.getInt(0,99);
                    if(randInt >= 0 && randInt < 49) {
                        gender = "M";
                    } else if(randInt >= 49 && randInt < 98) {
                        gender = "F";
                    } else {
                        gender = "U";
                    }
                    Integer addressid = addresses.get(i).id;
                    Long datestart = getRandDate("1980/01/01 00:00:00", "2018/01/01 00:00:00");
                    Long dateend = null;
                    Entity entity = new Entity(govid++,2,gender,namefirst,namelast,phone,email,addressid,cid,datestart,dateend);
                    entities.add(country, entity);
                    ctrEntities++;
                }
            }
            int numHomeless = ctrEntities *
                utils.getInt((int)(ProfileDG.MIN_HOMELESS_DENSITY * 100),(int)(ProfileDG.MAX_HOMELESS_DENSITY * 100))/100;
            for(int i = 0; i < numHomeless; i++) {
                String gender = utils.getRandString("MFU", 1);
                String namefirst = utils.getRandString(CHARSET,3);
                String namelast = utils.getRandString(CHARSET,3);
                String phone = utils.getBool() ? null : String.format("%05d%05d", country.id,phoneCtr++);
                String email = utils.getBool() ? null : String.format("%s.%s.%d@%s",namefirst,namelast,emailCtr++,cAbbr);
                Integer addressid = null;
                Long datestart = getRandDate("1980/01/01 00:00:00", "2018/01/01 00:00:00");
                Long dateend = null;
                Entity entity = new Entity(govid++,2,gender,namefirst,namelast,phone,email,addressid,cid,datestart,dateend);
                entities.add(country, entity);
            }
            if(entities.getPeople(country) == null) {
                throw new ParseException(
                    String.format("no people in country %s numhousehold %d numcompanies %d numhomeless %d numaddress:%d",
                        country.name, numHouseholds, numCompanies, numHomeless, numAddresses),0);
            }
        }

        public Departments genDepartments(Company company) {
            Departments departments = new Departments();
            int ctr = 0;
            int companySize = utils.getInt(1,10);
            int numGroups = 1;
            company.setCompanySize(companySize);
            if(companySize >= 0) {
                departments.add(new Department(company.id, ++ctr, "Executive Officials", "Executive"));
                departments.add(new Department(company.id, ++ctr, "Sales", "Finance"));
                departments.add(new Department(company.id, ++ctr, "Product", "R&D"));
                numGroups = utils.getInt(1,2);
            }
            if(companySize >= 3) {
                departments.add(new Department(company.id, ++ctr, "Finance", "Finance"));
                departments.add(new Department(company.id, ++ctr, "Marketing", "Finance"));
                departments.add(new Department(company.id, ++ctr, "Human Resources", "HR"));
                departments.add(new Department(company.id, ++ctr, "Routing", "Operations"));
                numGroups = utils.getInt(2,6);
            }
            if(companySize >= 7) {
                departments.add(new Department(company.id, ++ctr, "Director Officials", "Board"));
                departments.add(new Department(company.id, ++ctr, "Legal", "Legal"));
                departments.add(new Department(company.id, ++ctr, "Audit", "Finance"));
                departments.add(new Department(company.id, ++ctr, "Logistics", "Operations"));
                departments.add(new Department(company.id, ++ctr, "Research", "R&D"));
                numGroups = utils.getInt(6,12);
            }
            for(int i = 0; i < numGroups; i++) {
                departments.add(new Department(company.id, ++ctr, "Group"+i, "R&D"));
            }
            return departments;
        }

        public Companies genCompanies(Entities entities) {
            Companies companies = new Companies();
            for(Country country: entities.getCountries()) {
                Collection<Entity> entitiesInCountry = entities.get(country);
                for(Entity entity: entitiesInCountry) {
                    if(entity.entityType != 1) continue;
                    String domainname = entity.namelast + "." + country.abbrev;
                    Company company = new Company(entity.govid,entity.entityType,entity.namelast,entity.addressid,entity.phone,domainname,entity.datestart,entity.govid,country.id);
                    Departments departments = genDepartments(company);
                    company.setDepartments(departments);
                    companies.add(country, company);
                }
            }
            return companies;
        }

        void genEmployeesFillAllDepartments(LinkedList<Entity> entitiesLocal, List<Company> companiesLocal) throws ParseException {
            // validate
            {
                int numDept = 0;
                for(Company company: companiesLocal)
                    for(Department department: company.departments.get())
                        numDept++;
                int numEntities = entitiesLocal.size();
                numDept *= 2; // one for csuite and one for dept
                if(numDept > numEntities) {
                    throw new ParseException(String.format("numdept %d > numentities %d", numDept, numEntities), 0);
                }
            }
            // populate all departments for all companies first. then fill remainder randomly.
            for(Company company: companiesLocal) {
                while(!company.allDeptPopulated()) {
                    Entity entity = entitiesLocal.pop();
                    int idx = company.getEmployees().size();
                    String email = entity.namefirst + "." + entity.namelast + "." + idx + "@" + company.name + "." + company.countrycode;
                    Employee employee = new Employee(company.id, idx, entity.id, company.countrycode, email);
                    company.addEmployee(employee);
                }
            }

        }
        public void genInternationalEmployees(List<Company> listCompanies, Map<Country, LinkedList<Entity>> internationalEntities) {
            // populate internationals
            {
                List<Company> listLCo = new ArrayList<>();
                List<Company> listMCo = new ArrayList<>();
                for(Company company: listCompanies) {
                    if(company.getCompanySize() >= 7)       listLCo.add(company);
                    else if(company.getCompanySize() >= 4)  listMCo.add(company);
                }
                if(listLCo.size() == 0 && listMCo.size() == 0) {
                    System.out.printf("L and M companies do not exist\n");
                    return;
                }
                for(Country country: internationalEntities.keySet()) {
                    LinkedList<Entity> l = internationalEntities.get(country);
                    while(l.size() > 0) {
                        Company company = null;
                        if(utils.getInt(0,10) < 7 && listLCo.size() != 0) {
                            company = listLCo.get(utils.getInt(0, listLCo.size()-1));
                        } else {
                            company = listMCo.get(utils.getInt(0, listMCo.size()-1));
                        }
                        Entity entity = l.pop();
                        int idx = company.getEmployees().size();
                        String email = entity.namefirst + "." + entity.namelast + "." + idx + "@" + company.name + "." + company.countrycode;
                        Employee employee = new Employee(company.id, idx, entity.id, company.countrycode, email);
                        company.addEmployee(employee);
                    }
                }
            }
        }

        public void genEmployees(Entities entities, Companies companies) throws ParseException {
            Map<Country, LinkedList<Entity>> internationalEntities = new HashMap<>();
            List<Company> listCompanies = new ArrayList<>();
            for(Country country: entities.getCountries()) {
                LinkedList<Entity> entitiesLocal = new LinkedList<>();
                for(Entity e: entities.getPeople(country)) {
                    entitiesLocal.add(e);
                }

                int unEmploymentRate = utils.getInt(ProfileDG.MIN_UNEMPLOY,ProfileDG.MAX_UNEMPLOY);
                int numUnemployed = entitiesLocal.size() * unEmploymentRate / 100;
                for(int i = 0; i < numUnemployed; i++) {
                    entitiesLocal.pop();
                }

                List<Company> companiesLocal = new ArrayList<>(companies.get(country));
                listCompanies.addAll(companiesLocal);

                if(ProfileDG.FILL_ALL_DEPARTMENTS) {
                    genEmployeesFillAllDepartments(entitiesLocal, companiesLocal);
                }

                // reserve some internationals
                int numInternational = entitiesLocal.size()/utils.getInt(ProfileDG.MIN_INTERNATIONAL,ProfileDG.MAX_INTERNATIONAL) + 1;
                LinkedList<Entity> listInternational = new LinkedList<>();
                for(int i = 0; i < numInternational; i++) {
                    listInternational.add(entitiesLocal.pop());
                }
                internationalEntities.put(country, listInternational);

                // use remaining to populate domestic companies
                while(entitiesLocal.size() > 0) {
                    Entity entity = entitiesLocal.pop();
                    Company company = companiesLocal.get(utils.getInt(0, companiesLocal.size()-1));
                    int idx = company.getEmployees().size();
                    String email = entity.namefirst + "." + entity.namelast + "." + idx + "@" + company.name + "." + company.countrycode;
                    Employee employee = new Employee(company.id, idx, entity.id, company.countrycode, email);
                    company.addEmployee(employee);
                }
            }
            genInternationalEmployees(listCompanies, internationalEntities);
        }
        public void
        genContacts(Entities entities) {

        }
        public void
        genCustomers(Entities entities, Companies companies) {

        }
        public void
        genMigration(Entities entities)
        {

        }
        public void
        genEducation(Entities entities)
        {

        }
        public void
        genSkillset(Entities entities)
        {

        }
        public void
        genIncomes(Entities entities)
        {

        }
        public Products
        genProducts(Companies companies) throws Exception {
            return miniGenProducts(companies, 7);
        }

        public Products miniGenProducts(Companies companies, int numItemTypes) throws Exception {
            class PairNum {
                int lo;
                int hi;
                int category;
                PairNum(int lo, int hi) {
                    this.lo = lo;
                    this.hi = hi;
                }
            }
            class Helper {
                int category(List<PairNum> l, int v) {
                    for(PairNum p: l)
                        if(v >= p.lo && v <= p.hi) return p.category;
                    return 0;
                }
                Map<Integer,Integer> selectUniqueCategoriesAndVersions(Utils utils, List<PairNum> listPair, int maxRange, int numCategoriesToSelect) {
                    Map<Integer,Integer> categories = new HashMap<>();
                    List<Integer> listCategories = new ArrayList<>();
                    for(PairNum pn: listPair) {
                        listCategories.add(pn.category);
                    }
                    utils.shuffle(listCategories);
                    for(int i = 0; i < numCategoriesToSelect; i++) {
                        int category = listCategories.get(i);
                        int numVersions = utils.getInt(1,5);
                        categories.put(category, numVersions);
                    }
                    return categories;
                }
            }
            Helper h = new Helper();

            // ListPair is used to generate random distribution of category goods, not abstracting what the goods actually are.
            // This emulates that some things are manufactured more than others, eg paper is manufacturered
            // more than glass plates.
            // eg       pair1   00,08       // manufactured 9/16 frequency
            //          pair2   09,12       // manufactured 4/16 frequency
            //          pair3   13,13       // manufactured 1/16 frequency
            //          pair4   14,15       // manufactured 2/16 frequency
            int maxRange = 0;
            if(numItemTypes <= 2) {
                throw new Exception("numItemTypes too low " + numItemTypes);
            }

            List<PairNum> listPair = new ArrayList<>();
            for(int i = 0, beg = 0; i < numItemTypes; i++) {
                int cur = utils.getInt(2,20) + beg;
                PairNum pair = new PairNum(beg,cur);
                pair.category = i;
                listPair.add(pair);
                maxRange = cur;
                beg = cur + 1;
            }

            Map<Integer, Integer> mapBasePrice = new HashMap<>();
            for(int i = 0; i < numItemTypes; i++) {
                int basePrice = utils.getInt(200,2000);
                mapBasePrice.put(i, basePrice);
            }

            if(listPair.size() != numItemTypes) {
                throw new Exception(String.format("listPair size != numItemTypes -> %d != %d", listPair.size(), numItemTypes));
            }
            if(mapBasePrice.size() != numItemTypes) {
                throw new Exception(String.format("mapBasePrice size != numItemTypes -> %d != %d", mapBasePrice.size(), numItemTypes));
            }
            Products products = new Products();

            Map<Country, List<Company>> map = companies.get();
            int minNumCategories = 1;
            int maxNumCategories = numItemTypes/2;
            for(Map.Entry<Country, List<Company>> kv: map.entrySet()) {
                for(Company company: kv.getValue()) {
                    // each company might make multiple categories, and multiple versions of each category.
                    int numCategories = utils.getInt(minNumCategories, maxNumCategories);
                    Map<Integer,Integer> categories = h.selectUniqueCategoriesAndVersions(utils, listPair, maxRange, numCategories);
                    if(categories.size() == 0 || categories.size() != numCategories) {
                        throw new Exception(String.format("categories.size is invalid! -> %d,%d", categories.size(), numCategories));
                    }
                    for(Map.Entry<Integer,Integer> kvCategories: categories.entrySet()) {
                        int category = kvCategories.getKey();
                        int numVersions = kvCategories.getValue();
                        for(int i = 0; i < numVersions; i++) {
                            int version = i;
                            int quality = i;
                            String description = String.format("widget_%d_%d_%d_%d_%d", company.countrycode, company.id, category, version, quality);
                            Company manufacturer = company;
                            /*
                             * create cost vals:
                             * costQuality = (1+quality*0.2)*cost
                             * priceCost = (1 +/- 0-50%)*costQuality
                             * priceSale = (1 + 0-150%)*priceCost
                             */
                            int costBase = mapBasePrice.get(category);
                            int costQuality = ((100+(quality*20))*costBase)/100;
                            int priceCost = ((100+(utils.getBool() ? -1 : 1) * (utils.getInt(0,50))) * costQuality)/100;
                            int priceSale = ((100+(utils.getInt(1,150))) * priceCost)/100;
                            Company seller = company;
                            Product product = new
                                Product(category, version, quality, description, manufacturer, priceCost, priceSale, seller);
                            products.add(product, category);
                        }
                    }
                }
            }
            return products;
        }
        public SalesRecords
        genSalesRecords(Entities entities, Products products) throws Exception {
            return miniGenSalesRecords(entities, products);
        }
        public SalesRecords miniGenSalesRecords(Entities entities, Products products) throws Exception
        {
            class Helper {
                int [][] setDistribution(Products products) {
                    List<Integer> categories = new ArrayList<>(products.getCategories());
                    int numCategories = categories.size();
                    int numProducts = 0;
                    for(Integer category: categories) {
                        numProducts += products.getProducts(category).size();
                    }
                    int [][] dist = new int[numCategories][];
                    for(int i = 0; i < numCategories; i++) dist[i] = new int[3];
                    // number of products proportional to distribution for this dataset
                    int sumWeight = 0;
                    for(int i = 0; i < categories.size(); i++) {
                        Integer category = categories.get(i);
                        int numProductsInCategory = products.getProducts(category).size();
                        int weight = numProductsInCategory/numProducts;
                        if((i+1) == categories.size()) {
                            weight = 100 - sumWeight;
                        }
                        dist[i][0] = i;
                        dist[i][1] = i;
                        dist[i][2] = weight;
                        sumWeight += weight;
                    }
                    //for(int i = 0; i < dist.length; i++) {
                    //    System.out.printf("dist %2d %2d = %2d\n", dist[i][0],dist[i][1],dist[i][2]);
                    //}
                    return dist;
                }
                List<Product> getPurchaseProducts(
                    Set<Integer> setCountryIds,
                    int medianConsumptionQuality,
                    int medianPurchasingPower,
                    int medianRecords,
                    Products products,
                    int [][] dist) throws Exception
                {
                    List<Product> l = new ArrayList<>();
                    int individualConsumptionQuality = medianConsumptionQuality + utils.getInt(-1,1);
                    int individualPurchasingPower = utils.getInt(-2,2) + medianPurchasingPower;
                    individualPurchasingPower = (individualPurchasingPower < -7) ? -7 : individualPurchasingPower;
                    int individualPurchasingCount = medianRecords * (10 + individualPurchasingPower) / 10;
                    // individualPurchasingCount = number of products this entity buys from a list of categories and products
                    int numCategories = products.getCategories().size();
                    List<Integer> listCategoryPurchases = utils
                        .getInt(0,numCategories-1, dist, individualPurchasingCount);
                    for(Integer category: listCategoryPurchases) {
                        Set<Product> setProducts = products.getProducts(category);
                        List<Product> listProducts = products
                            .getProductsByCountriesAndQuality(category, setCountryIds, individualConsumptionQuality);
                        l.addAll(listProducts);
                    }
                    // from the resulting list of all possible categories and products filtered by individual
                    // preferences, randomly choose individualPurchasingCount items from this list.
                    List<Product> purchaseList = new ArrayList<>();
                    int maxItems = l.size();
                    for(int i = 0; i < individualPurchasingCount; i++) {
                        int idx = utils.getInt(0, maxItems);
                        if(idx > 0) {
                            Product p = l.get(idx-1);
                            purchaseList.add(p);
                        }
                    }
                    return purchaseList;
                }
                /*
                 * return set of country ids including selfCountry id, and includePercentage of Collection, which is (0<x<=1)
                 */
                Set<Integer> getSetCountryIds(Collection<Country> countries, Country selfCountry, double includePercentage) {
                    int numCountries = countries.size();
                    int maxNum = (int)(numCountries * includePercentage);
                    List<Integer> listCountryIds = new ArrayList<>();
                    for(Country country: countries) {
                        listCountryIds.add(country.id);
                    }
                    utils.shuffle(listCountryIds);
                    Set<Integer> setCountryIds = new HashSet<>();
                    setCountryIds.add(selfCountry.id);
                    for(int i = 0; i < listCountryIds.size() && setCountryIds.size() < maxNum; i++) {
                        setCountryIds.add(listCountryIds.get(i));
                    }
                    return setCountryIds;
                }
                void generateSalesRecords(List<Product> listProducts, Entity entity, SalesRecords salesRecords) throws ParseException {
                    int pctKnownEntity = 6; // 60%
                    int pctKnownCountry = 8; // 80%
                    int pctReceipts = 7; // 70%
                    long dateStartEntity = entity.datestart;
                    long dateEnd = DataGenerator.date2millis("2018/06/01 00:00:00");

                    for(int i = 0; i < listProducts.size(); i++) {
                        Product product = listProducts.get(i);
                        Long date = null;
                        int fromCountryId = product.company_country_id;
                        int fromCompanyId = product.company_id;
                        int quantity = utils.getInt(1,6);
                        int pricePerItem = product.priceSale;
                        int priceTotal = quantity * pricePerItem;
                        Integer toEntityId = utils.getInt(0,10) <= pctKnownEntity ? entity.id : null;
                        Integer toCountryId = utils.getInt(0,10) <= pctKnownCountry ? entity.countrycode : null;
                        int productId = product.id;
                        int version = product.version;
                        int categoryId = product.category;
                        Company company = product.manufacturer;
                        long dateStartCompany = entity.datestart;
                        if(utils.getInt(0,10) <= 9) {
                            long dateMoreRecent = dateStartEntity > dateStartCompany ? dateStartEntity : dateStartCompany;
                            long dateDiff = dateEnd - dateMoreRecent;
                            date = utils.getLong(0L, dateDiff) + dateMoreRecent;
                        }
                        Integer receiptId = company.getNextReceiptId();
                        SalesRecord record = new SalesRecord(date, receiptId, fromCountryId, fromCompanyId, quantity, pricePerItem, priceTotal, toEntityId, toCountryId, productId, categoryId, version);
                        salesRecords.add(company, record);
                        if(utils.getInt(0,10) <= pctReceipts) {
                            ReceiptsRecord receipt = new ReceiptsRecord(date, receiptId, fromCountryId, fromCompanyId, quantity, pricePerItem, priceTotal, toEntityId, toCountryId, productId, categoryId);
                            salesRecords.add(entity, receipt);
                        }
                    }
                }
            }
            Collection<Country> countries = entities.getCountries();
            int numEntities = 0;
            for(Country country: countries) {
                Collection<Entity> entitiesGroup = entities.getPeople(country);
                numEntities += entitiesGroup.size();
            }
            int medianRecords = 5;
            Helper h = new Helper();
            SalesRecords salesRecords = new SalesRecords();
            int [][] dist = h.setDistribution(products);
            Collection<Country> setCountries = entities.getCountries();
            for(Country country: setCountries) {
                int medianConsumption = utils.getInt(1,3);
                Set<Integer> setCountryIds = h.getSetCountryIds(setCountries, country, 0.5);
                for(Entity entity: entities.getPeople(country)) {
                    List<Product> listProducts = h.getPurchaseProducts(
                        setCountryIds, medianConsumption, medianConsumption, medianRecords, products, dist);
                    h.generateSalesRecords(listProducts, entity, salesRecords);
                }
            }
            return salesRecords;
        }
        public void
        genInventories()
        {

        }
        public void
        genTransactions()
        {

        }
        public void
        genSalesPoints()
        {

        }
        public void
        genBanks()
        {

        }
    }

    public void p(String f, Object ...o) {
        logger.p(f,o);
    }

    public void testGenerateSQLMap1() throws Exception {
        DataGenerator gen = new DataGenerator();
        Countries countries = gen.genCountries();
        List<MyTab> keys = Arrays.asList(MyTab.COUNTRY,MyTab.PROVINCE,MyTab.CITY,MyTab.STREET,MyTab.ADDRESS,MyTab.ENTITY,MyTab.EMPLOYEE,MyTab.COMPANY,MyTab.DEPARTMENT,MyTab.PRODUCT,MyTab.RECEIPTS_SELL,MyTab.RECEIPTS_BUY);
        Map<MyTab,List<String>> tables = new HashMap<>();
        for(MyTab key: keys) {
            tables.put(key, new ArrayList<>());
        }
        Data2SQL data2SQL = new Data2SQL();
        data2SQL.getSQLCountries(tables, countries);
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.COUNTRY)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.PROVINCE)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.CITY)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.STREET)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.ADDRESS)) { p("%s\n", s); }

        Entities entities = gen.genEntities(countries);
        data2SQL.getSQLEntities(tables, entities);
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.ENTITY)) { p("%s\n", s); }

        Companies companies = gen.genCompanies(entities);
        gen.genEmployees(entities, companies);
        data2SQL.getSQLCompanies(tables, companies);
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.COMPANY)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.DEPARTMENT)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.EMPLOYEE)) { p("%s\n", s); }

        Products products = gen.genProducts(companies);
        data2SQL.getSQLProducts(tables, products);
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.PRODUCT)) { p("%s\n", s); }

        SalesRecords salesRecords = gen.genSalesRecords(entities, products);
        data2SQL.getSQLSalesRecords(tables, salesRecords);
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.RECEIPTS_SELL)) { p("%s\n", s); }
        //p("------------------------------------------------\n");
        for(String s: tables.get(MyTab.RECEIPTS_BUY)) { p("%s\n", s); }
    }

    public void testDate() throws ParseException {
        String dateIn ="2018/01/01 12:34:56";
        long ms = DataGenerator.date2millis(dateIn);
        String dateOut = DataGenerator.millis2date(ms);
        p("dateIn:%s ms:%d dateOut:%s\n", dateIn, ms, dateOut);
    }

}
enum MyTab {
    COUNTRY,
    PROVINCE,
    CITY,
    STREET,
    ADDRESS,
    ENTITY,
    EMPLOYEE,
    COMPANY,
    DEPARTMENT,
    PRODUCT,
    RECEIPTS_BUY,
    RECEIPTS_SELL,
}
enum EntityType {
    COMPANY,
    PERSON,
    ANIMAL,
    GOVERNMENT
}
enum CompanyType {
    COMPANY_PROFIT,
    COMPANY_TAXFREE_RELIGION,
    COMPANY_PUBLIC_SCHOOL_PRIM,
    COMPANY_PUBLIC_SCHOOL_SEC,
    COMPANY_PUBLIC_SCHOOL_COLLEGE,
    COMPANY_PUBLIC_SCHOOL_UNIVERSITY,
    COMPANY_PRIVATE_SCHOOL_PRIM,
    COMPANY_PRIVATE_SCHOOL_SEC,
    COMPANY_PRIVATE_SCHOOL_COLLEGE,
    COMPANY_PRIVATE_SCHOOL_UNIVERSITY,
    COMPANY_HOSPITAL,
    COMPANY_NONPROFIT
}
enum SkillsetEducation {
    ECONOMICS,
    POLITICAL,
    MUSIC,
    JOURNALISM,
    EDUCATION,
    BUSINESS,
    MISC,
    BIOLOGY,
    PHYSICS,
    MATH,
    CS,
    CHEMISTRY,
    ENG_MECHANICAL,
    ENG_NUCLEAR,
    ENG_ELECTRICAL,
    ENG_MATERIALS,
    ENG_CHEMICAL,
    MEDICINE,
    LAW
}
enum SkillsetDegree {
    UNDER_HS,
    HS,
    BACHELOR,
    MASTER,
    DOCTORATE,
    POST_DOCTORATE
}
enum WidgetType {
    TOY,
    ART,
    TV,
    TOOL_SPECIFIC,
    TOOL_GENERAL,
    TOOL_KITCHEN,
    TOOL_LIVING,
    TOOL_AUTO,
    TOOL_OUTDOOR_MAINTAIN,
    TOOL_OUTDOOR_RECREATE,
    TOOL_BATH,
    TOOL_BED,
    SERVICES_FOOD,
    SERVICES_FOOD_SOCIAL,
    SERVICES_SPORTS,
    SERVICES_SPORTS_SOCIAL,
    SERVICES_CLUBS,
    SERVICES_CLUBS_SOCIAL,
    SERVICES_MISC,
    SERVICES_MISC_SOCIAL,
    SERVICES_FINANCIAL
}

class ProfileDG {
    public static final int MIN_UNEMPLOY = 25;
    public static final int MAX_UNEMPLOY = 60;
    public static final int MIN_INTERNATIONAL = 2;
    public static final int MAX_INTERNATIONAL = 80;
    public static final double MIN_HOMELESS_DENSITY = 0.05;
    public static final double MAX_HOMELESS_DENSITY = 0.10;
    public static final int MIN_ENTITY_PER_ADDRESS = 1;
    public static final int MAX_ENTITY_PER_ADDRESS = 5;
    public static final double MIN_PCT_ENTITY_COMPANY = 0.05;
    public static final double MAX_PCT_ENTITY_COMPANY = 0.1;
    public static final int MIN_UNITS_PER_ADDRESS = 2;
    public static final int MAX_UNITS_PER_ADDRESS = 9;
    public static final int MIN_PROVINCE = 2;
    public static final int MAX_PROVINCE = 5;
    public static final int MIN_CITY_PER_PROVINCE = 2;
    public static final int MAX_CITY_PER_PROVINCE = 6;
    public static final int MIN_STREET_PER_CITY = 1;
    public static final int MAX_STREET_PER_CITY = 5;
    public static final int MIN_ADDR_PER_STREET = 2;
    public static final int MAX_ADDR_PER_STREET = 6;
    public static final int MIN_COUNTRIES = 4;
    public static final int MAX_COUNTRIES = 7;
    public static final int MIN_DENSITY_POPULATION = 10;
    public static final int MAX_DENSITY_POPULATION = 50;
    public static final boolean FILL_ALL_DEPARTMENTS = false;
    public static final boolean USE_RANDOM = true;
}


