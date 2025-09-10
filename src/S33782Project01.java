import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class S33782Project01 {
    public static void main(String[] args) {

        args = MainMenu.menu();

        ArgsValidation.validate(args);

        DataType dataType = ConvertStringToEnum.convert(DataType.class, args[0]);
        CollectionType collectionType = ConvertStringToEnum.convert(CollectionType.class, args[1]);
        int size = SizeSelector.selectSize(args[2]);
        TestType testType = ConvertStringToEnum.convert(TestType.class, args[3]);
        OutputType outputType = ConvertStringToEnum.convert(OutputType.class, args[4]);

        TestResult testResult = TestSelector.selectTest(testType, collectionType, size, dataType);

        Presenter.displayResult(testResult, outputType, testType);


    }
    public static int seed = 123456;
    public static String csvPath = "D:\\Studia\\2 Semestr\\GUI\\CSV.txt";
}

interface ValidateParameter {
    void validate(String parameter);

    // <E extends Enum<E>> vs <E extends Enum> - peirwsza wersja jasno mówi kompilatorowi, z jakim typem Enuma będzie
    // pracować
    static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String value) {
        // e.name() - metoda dla enumów zwracająca nazwę enuma jako String
        return Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value.toUpperCase()));
    }

    static void parameterBaseCheck(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            throw new IllegalArgumentException("Parameter cannot be null or empty");
        }
    }
}

class ArgsValidation {

    public static void validate(String[] args) {

        if (args.length != 5)
            throw new IllegalArgumentException("Invalid number of arguments");

        ValidateDataType validateDataType = new ValidateDataType();
        validateDataType.validate(args[0]);

        ValidateCollectionType validateCollectionType = new ValidateCollectionType();
        validateCollectionType.validate(args[1]);
        if (
                args[3].equalsIgnoreCase("GET_BY_INDEX")
                        && (args[1].equalsIgnoreCase("HASHSET")
                        || args[1].equalsIgnoreCase("TREESET"))
        )
            throw new IllegalArgumentException("Get by index is not supported for HashSet and TreeSet");

        ValidateCollectionSize validateCollectionSize = new ValidateCollectionSize();
        validateCollectionSize.validate(args[2]);

        ValidateTestType validateTestType = new ValidateTestType();
        validateTestType.validate(args[3]);

        ValidateOutputType validateOutputType = new ValidateOutputType();
        validateOutputType.validate(args[4]);

        System.out.println("Parameters accepted");
    }
}

class ValidateCollectionSize
        implements ValidateParameter {

    public void validate(String parameter) {
        ValidateParameter.parameterBaseCheck(parameter);

        if (
                !(ValidateParameter.isValidEnumValue(CollectionSize.class, parameter)
                        || parameter.matches("\\d+"))
        )
            throw new IllegalArgumentException("Invalid collection size");

        if (parameter.equalsIgnoreCase("CUSTOM"))
            throw new IllegalArgumentException(
                    "Choose one of s100, s500, s1000, s10000 or input custom size (e.g. 1200)"
            );

        if (parameter.matches("0"))
            throw new IllegalArgumentException("Collection size must be greater than 0");
    }
}

class ValidateCollectionType
        implements ValidateParameter {

    public void validate(String parameter) {
        ValidateParameter.parameterBaseCheck(parameter);

        if (!ValidateParameter.isValidEnumValue(CollectionType.class, parameter))
            throw new IllegalArgumentException("Invalid collection type");
    }
}

class ValidateDataType
        implements ValidateParameter {

    @Override
    public void validate(String parameter) {
        ValidateParameter.parameterBaseCheck(parameter);

        if (!ValidateParameter.isValidEnumValue(DataType.class, parameter))
            throw new IllegalArgumentException("Invalid data type");
    }
}

class ValidateOutputType
        implements ValidateParameter {

    public void validate(String parameter) {
        ValidateParameter.parameterBaseCheck(parameter);

        if (!ValidateParameter.isValidEnumValue(OutputType.class, parameter))
            throw new IllegalArgumentException("Invalid output type");
    }
}

class ValidateTestType
        implements ValidateParameter {

    public void validate(String parameter) {
        ValidateParameter.parameterBaseCheck(parameter);

        if (!ValidateParameter.isValidEnumValue(TestType.class, parameter))
            throw new IllegalArgumentException("Invalid test type");
    }
}

class Car
        implements Comparable<Car> {

    private final String brand;
    private final int yearOfProduction;
    private final int cylinderNum;

    public Car(String brand, int year, int cylinderNum) {
        this.brand = brand;
        this.yearOfProduction = year;
        this.cylinderNum = cylinderNum;
    }

    public String getBrand() {
        return brand;
    }
    public int getYearOfProduction() {
        return yearOfProduction;
    }
    public int getCylinderNum() {
        return cylinderNum;
    }

    public int compareTo(Car otherCar) {
        int out = this.yearOfProduction - otherCar.yearOfProduction;
        if (out != 0)
            return out;
        out = cylinderNum - otherCar.cylinderNum;
        if (out != 0)
            return out;
        return brand.compareTo(otherCar.brand);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return yearOfProduction == car.yearOfProduction
                && cylinderNum == car.cylinderNum
                && Objects.equals(brand, car.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, yearOfProduction, cylinderNum);
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", yearOfProduction=" + yearOfProduction +
                ", cylinderNum=" + cylinderNum +
                '}';
    }
}

class MyColor
        extends Color
        implements Comparable<MyColor> {

    private final int sum;

    public MyColor(int r, int g, int b) {
        super(r, g, b);
        this.sum = r + g + b;
    }

    public int getSum() {
        return sum;
    }


    @Override
    public int compareTo(MyColor o) {
        return this.getSum() - o.getSum();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MyColor myColor = (MyColor) o;
        return sum == myColor.sum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sum);
    }

    @Override
    public String toString() {
        return "MyColor{" +
                "sum: " + sum +
                ", red: " + this.getRed() +
                ", green: " + this.getGreen() +
                ", blue: " + this.getBlue() +
                '}';
    }
}

class Person
        implements Comparable<Person>{

    private final String name;
    private final String surname;
    private final int yearOfBirth;

    public Person(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.yearOfBirth = age;
    }

    public String getName() {
        return name;
    }
    public String getSurname() { return surname; }
    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public int compareTo (Person otherPerson) {
        int out = this.yearOfBirth - otherPerson.yearOfBirth;
        if (out != 0)
            return out;
        out = this.name.compareTo(otherPerson.name);
        if (out != 0)
            return out;
        return this.surname.compareTo(otherPerson.surname);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        // Rzutujemy na typ Person, aby móc korzystać z pól klasowych, bo wcześniej o jest traktowane przez kompilator
        // jako object
        Person person = (Person) o;
        return yearOfBirth == person.yearOfBirth
                && Objects.equals(name, person.name)
                && Objects.equals(surname, person.surname);
    }

    @Override
    public int hashCode() {
        // .hash() działa dla więcej niż 1 argumentu (tworzy tablicę z obiektów i na jej podstawie oblicza, .hashCode()
        // dla tylko jednego
        return Objects.hash(name, surname, yearOfBirth);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}

class Screen
        implements Comparable<Screen> {

    private final int diagonal;
    private final int refreshRate;
    private final int modelNumber;
    private final int yearOfProduction;

    public Screen(int resolution, int refreshRate, int modelNumber, int yearOfProduction) {
        this.diagonal = resolution;
        this.refreshRate = refreshRate;
        this.modelNumber = modelNumber;
        this.yearOfProduction = yearOfProduction;
    }

    public int getDiagonal() {
        return diagonal;
    }
    public int getRefreshRate() {
        return refreshRate;
    }
    public int getModelNumber() {
        return modelNumber;
    }
    public int getYearOfProduction() { return yearOfProduction; }


    public int compareTo(Screen otherScreen) {
        int out = this.diagonal - otherScreen.diagonal;
        if (out != 0)
            return out;
        out = this.refreshRate - otherScreen.refreshRate;
        if (out != 0)
            return out;
        out = this.modelNumber - otherScreen.modelNumber;
        if (out != 0)
            return out;
        return this.yearOfProduction - otherScreen.yearOfProduction;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Screen screen = (Screen) o;
        return diagonal == screen.diagonal
                && refreshRate == screen.refreshRate
                && modelNumber == screen.modelNumber
                && yearOfProduction == screen.yearOfProduction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagonal, refreshRate, modelNumber, yearOfProduction);
    }

    @Override
    public String toString() {
        return "Screen{" +
                "diagonal=" + diagonal +
                ", refreshRate=" + refreshRate +
                ", modelNumber=" + modelNumber +
                ", yearOfProduction=" + yearOfProduction +
                '}';
    }
}

class CollectionSizeMenu {
    public static String open() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select size out of:");
        CollectionSize[] types = CollectionSize.values();
        for (int i = 0; i < types.length-1; i++) {
            System.out.println(types[i]);
        }
        System.out.println("Or input number: ");

        return sc.nextLine();
    }
}

class CollectionTypeMenu {
    public static String open() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select collection out of:");
        CollectionType[] types = CollectionType.values();
        for (CollectionType type : types) {
            System.out.println(type);
        }
        return sc.nextLine();
    }
}

class DataTypeMenu {
    public static String open() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select data out of:");
        DataType[] types = DataType.values();
        for (DataType type : types) {
            System.out.println(type);
        }
        return sc.nextLine();
    }
}

class MainMenu {
    public static String[] menu() {
        Scanner sc = new Scanner(System.in);
        String[] out = new String[5];
        boolean runTest = false;

        do {
            // zastąpiłem konkatenacje podpowiedzią z intellij, który zamienił poprzedniego printa na to:
            System.out.println(
                    """
                            Choose an option(number):
                            1. Data type
                            2. Collection type
                            3. Collection size
                            4. Test type
                            5. Output type
                            6. Run test
                            """
            );
            int option = sc.nextInt();

            switch (option) {
                case 1 -> out[0] = DataTypeMenu.open();
                case 2 -> out[1] = CollectionTypeMenu.open();
                case 3 -> out[2] = CollectionSizeMenu.open();
                case 4 -> out[3] = TestTypeMenu.open();
                case 5 -> out[4] = OutputTypeMenu.open();
                case 6 -> runTest = true;
            }
        } while (!runTest);

        return out;
    }
}

class OutputTypeMenu {
    public static String open() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select output type out of:");
        OutputType[] types = OutputType.values();
        for (OutputType type : types) {
            System.out.println(type);
        }
        return sc.nextLine();
    }
}

class TestTypeMenu {
    public static String open() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select test out of:");
        TestType[] types = TestType.values();
        for (TestType type : types) {
            System.out.println(type);
        }
        return sc.nextLine();
    }
}

interface ObjectGenerator<T> {
    T generate();
}


class CarGenerator
        implements ObjectGenerator<Car> {
    private final RandomEnumGenerator<CarBrand> brands = new RandomEnumGenerator<>(CarBrand.class);
    private final Random rand = new Random(S33782Project01.seed);

    public Car generate() {
        String brand = brands.randEnum().toString();

        int[] numbers = rand.ints(2)
                .map(i -> i==0 ? rand.nextInt(1930, 10000) : rand.nextInt(2, 19))
                .toArray();

        int yearOfProduction = numbers[0];
        int cylinders = numbers[1];
        return new Car(brand, yearOfProduction, cylinders);
    }
}

class ColorGenerator
        implements ObjectGenerator<MyColor> {
    private final Random rand = new Random(S33782Project01.seed);

    @Override
    public MyColor generate() {

        int[] rgb = rand.ints(3, 0, 256)
                .toArray();

        return new MyColor(rgb[0], rgb[1], rgb[2]);
    }

}

class DoubleGenerator
        implements ObjectGenerator<Double>{
    private final Random rand = new Random(S33782Project01.seed);

    public Double generate() {
        return rand.doubles(1)
                .findFirst()
                .orElseThrow();
    }
}

class IntegerGenerator
        implements ObjectGenerator<Integer>{
    private final Random rand = new Random(S33782Project01.seed);

    public Integer generate() {
        return rand.ints(1, 0, Integer.MAX_VALUE)
                .findFirst()// opakowuje
                .orElseThrow(); // dopiero to zwraca liczbę
    }
}

class ObjectSelector {
    public static ObjectGenerator<?> getObjectGenerator(DataType dataType) {
        return switch (dataType) {
            case INTEGER -> new IntegerGenerator();
            case DOUBLE -> new DoubleGenerator();
            case CAR -> new CarGenerator();
            case MYCOLOR -> new ColorGenerator();
            case PERSON -> new PersonGenerator();
            case SCREEN -> new ScreenGenerator();
        };
    }
}

class PersonGenerator
        implements ObjectGenerator<Person> {
    private final RandomEnumGenerator<PeopleNames> names = new RandomEnumGenerator<>(PeopleNames.class);
    private final RandomEnumGenerator<PeopleSurnames> surnames = new RandomEnumGenerator<>(PeopleSurnames.class);
    private final Random rand = new Random(S33782Project01.seed);


    public Person generate() {
        String name = names.randEnum().toString();
        String surname = surnames.randEnum().toString();

        int yearOfBirth = rand.ints(1, 0, 1000)
                .findFirst()
                .orElseThrow();

        return new Person(name, surname, yearOfBirth);
    }
}

class ScreenGenerator
        implements ObjectGenerator<Screen> {
    private final Random rand = new Random(S33782Project01.seed);
    private final int[] diagonals = {10, 13, 15, 17, 19, 21, 23, 24, 27, 32, 34, 36, 38, 40, 43, 49, 50, 55, 60, 65, 75, 85, 98};
    private final int[] refreshRates = {50, 60, 75, 85, 100, 120, 144, 165, 200, 240, 300, 360, 480};


    public Screen generate() {
        int diagonalIndex = rand.ints(1, diagonals.length)
                .findFirst()
                .orElseThrow();
        int refreshRateIndex = rand.ints(1, refreshRates.length)
                .findFirst()
                .orElseThrow();

        int diagonal = diagonals[diagonalIndex];
        int refreshRate = refreshRates[refreshRateIndex];

        int[] numbers = rand.ints(2)
                .map(i -> i==0 ? rand.nextInt(0, 1000) : rand.nextInt(1960, 10000))
                .toArray();
        int modelNumber = numbers[0];
        int yearOfProduction = numbers[1];

        return new Screen(
                diagonal,
                refreshRate,
                modelNumber,
                yearOfProduction
        );
    }
}

interface CollectionGenerator<T> {
    Collection<T> generateCollection(int size);
}

class ArrayListGenerator<T>
        implements CollectionGenerator<T> {

    private final ObjectGenerator<T> generator;

    public ArrayListGenerator(ObjectGenerator<T> generator) {
        this.generator = generator;
    }

    public Collection<T> generateCollection (int size) {
        ArrayList<T> list = new ArrayList<>();
        do {
            list.add(generator.generate());
        } while (list.size() < size);
        return list;
    }
}

class
HashSetGenerator<T>
        implements CollectionGenerator<T> {

    private final ObjectGenerator<T> generator;

    public HashSetGenerator(ObjectGenerator<T> generator) {
        this.generator = generator;
    }

    public Collection<T> generateCollection (int size) {
        HashSet<T> set = new HashSet<>();
        do {
            set.add(generator.generate());
        } while (set.size() < size);
        return set;
    }
}

class
LinkedListGenerator<T>
        implements CollectionGenerator<T> {

    private final ObjectGenerator<T> generator;

    public LinkedListGenerator(ObjectGenerator<T> generator) {
        this.generator = generator;
    }

    public Collection<T> generateCollection (int size) {
        LinkedList<T> list = new LinkedList<>();
        do {
            list.add(generator.generate());
        } while (list.size() < size);
        return list;
    }
}

class
    TreeSetGenerator<T>
    implements CollectionGenerator<T> {

    private final ObjectGenerator<T> generator;

    public TreeSetGenerator(ObjectGenerator<T> generator) {
        this.generator = generator;
    }

    public Collection<T> generateCollection (int size) {
        TreeSet<T> set = new TreeSet<>();
        do {
            set.add(generator.generate());
        } while (set.size() < size);
        return set;
    }
}

class CollectionFiller {
    public static Collection<?> fill(CollectionType collectionType, int size, DataType dataType) {
        ObjectGenerator<?> objectGenerator = ObjectSelector.getObjectGenerator(dataType);
        CollectionGenerator<?> collectionGenerator = CollectionSelector.getCollectionGenerator(collectionType, objectGenerator);
        return collectionGenerator.generateCollection(size);
    }
}

class CollectionSelector {
    public static CollectionGenerator<?> getCollectionGenerator(
            CollectionType collectionType,
            ObjectGenerator<?> objectGenerator) {

        return switch (collectionType) {
            case ARRAYLIST -> new ArrayListGenerator<>(objectGenerator);
            case LINKEDLIST -> new LinkedListGenerator<>(objectGenerator);
            case HASHSET -> new HashSetGenerator<>(objectGenerator);
            case TREESET -> new TreeSetGenerator<>(objectGenerator);
        };
    }
}

class SizeSelector {

    public static int selectSize(String size) {
        return switch (size.toUpperCase()) {
            case "S100" -> 100;
            case "S500" -> 500;
            case "S1000" -> 1000;
            case "S10000" -> 10000;
            default -> Integer.parseInt(size);
        };
    }
}

class TestSelector {
    public static TestResult selectTest(
            TestType testType,
            CollectionType collectionType,
            int size,
            DataType dataType
    ) {
        return switch (testType) {
            case GET_BY_INDEX -> {
                if (!collectionType.hasIndexes())
                    throw new IllegalArgumentException("Collection type doesn't have indexes");
                yield new GetByIndexTest().runTest(collectionType, size, dataType);
            }
            case ADD_REMOVE_FREQUENCY -> new AddRemoveFrequencyTest().runTest(collectionType, size, dataType);
            case GET_BY_VALUE -> new GetByValTest().runTest(collectionType, size, dataType);
            case CONTAINS -> new ContainsTest().runTest(collectionType, size, dataType);
        };
    }
}

interface Test {

    TestResult runTest(CollectionType collectionType, int size, DataType dataType);

    String getName();

    static long getClearTime(Collection<?> collection) {
        long startTime = System.nanoTime();
        collection.clear();
        return System.nanoTime() - startTime;
    }
}

class GetByValTest
        implements Test {

    public TestResult runTest(
            CollectionType collectionType,
            int size,
            DataType dataType
    ) {
        long startTime = System.nanoTime();
        Collection<?> collection = CollectionFiller.fill(collectionType, size, dataType);
        long addingTime = System.nanoTime() - startTime;

        ObjectGenerator<?> generator = ObjectSelector.getObjectGenerator(dataType);
        Object valToRemove = generator.generate();
        int sizeBefore = collection.size();
        boolean contains = false;

        long containsCheckStart = System.nanoTime();
        collection.removeIf(e -> e.equals(valToRemove));
        long containsCheckTime = System.nanoTime() - containsCheckStart;

        Long removeElementTime = null;
        if (collection.size() != sizeBefore) {
            contains = true;
            removeElementTime = containsCheckTime;
        }

        long clearTime = Test.getClearTime(collection);

        return new TestResult(
                addingTime, containsCheckTime,
                removeElementTime, contains,
                null, null,
                clearTime
        );
    }

    @Override
    public String getName() {
        return "Get by value";
    }
}

class GetByIndexTest
        implements Test {

    public TestResult runTest(CollectionType collectionType, int size, DataType dataType) {
        long startTime = System.nanoTime();
        Collection<?> collection = CollectionFiller.fill(collectionType, size, dataType);
        long addingTime = System.nanoTime() - startTime;

        Random rand = new Random(S33782Project01.seed);
        int index = rand.nextInt(size)-1;

        long searchStartTime = System.nanoTime();
        Object object = ((List<?>) collection).get(index);
        long searchTime = System.nanoTime() - searchStartTime;
        System.out.println("Object: " + object);

        long removeByIndexStartTime = System.nanoTime();
        ((List<?>) collection).remove(index);
        long removeByIndexTime = System.nanoTime() - removeByIndexStartTime;

        long clearTime = Test.getClearTime(collection);

        return new TestResult(addingTime, searchTime,
                removeByIndexTime, null,
                null, null,
                clearTime
        );
    }
    @Override
    public String getName() {
        return "Get by index";
    }
}

class ContainsTest
        implements Test {

    public TestResult runTest(
            CollectionType collectionType,
            int size,
            DataType dataType
    ) {
        long startTime = System.nanoTime();
        Collection<?> collection = CollectionFiller.fill(collectionType, size, dataType);
        long addingTime = System.nanoTime() - startTime;

        ObjectGenerator<?> generator = ObjectSelector.getObjectGenerator(dataType);
        Object valToFind = generator.generate();
        System.out.println("Object to find: " + valToFind);

        long containsCheckStart = System.nanoTime();
        boolean contains = collection.stream()
                .anyMatch(e -> e.equals(valToFind));
        long containsCheckTime = System.nanoTime() - containsCheckStart;

        long clearTime = Test.getClearTime(collection);

        return new TestResult(
                addingTime, containsCheckTime,
                null, contains,
                null, null,
                clearTime
        );
    }
    @Override
    public String getName() {
        return "Contains";
    }
}

class AddRemoveFrequencyTest
        implements Test {

    public TestResult runTest(CollectionType collectionType, int size, DataType dataType) {
        long startTime = System.nanoTime();
        Collection<?> collection = CollectionFiller.fill(collectionType, size, dataType);
        long addingTime = System.nanoTime() - startTime;

        double addingFrequency = (double) addingTime/size;

        long clearTime = Test.getClearTime(collection);
        double removingFrequency = (double) clearTime/size;

        return new TestResult(
                addingTime, null,
                null, null,
                addingFrequency, removingFrequency,
                clearTime);
    }
    @Override
    public String getName() {
        return "Add/remove frequency";
    }
}

class TestResult {
    private final Long addTime;
    private final Long searchTime;
    private final Long removeElementTime;
    private final Boolean contains;
    private final Double addFrequency;
    private final Double removeFrequency;
    private final Long collectionClearTime;

    public TestResult(
            Long addTime, Long searchTime,
            Long removeElementTime, Boolean contains,
            Double addFrequency, Double removeFrequency,
            Long clearTime
    ) {
        this.addTime = addTime;
        this.searchTime = searchTime;
        this.removeElementTime = removeElementTime;
        this.contains = contains;
        this.addFrequency = addFrequency;
        this.removeFrequency = removeFrequency;
        this.collectionClearTime = clearTime;
    }

    public Long getAddTime() {
        return addTime;
    }
    public Long getSearchTime() {
        return searchTime;
    }
    public Long getRemoveTime() {
        return removeElementTime;
    }
    public Boolean contains() {
        return contains;
    }
    public Double getAddFrequency() {
        return addFrequency;
    }
    public Double getRemoveFrequency() {
        return removeFrequency;
    }
    public Long getCollectionClearTime() {
        return collectionClearTime;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "addTime=" + addTime +
                "ns, searchTime=" + searchTime +
                "ns, removeTime=" + removeElementTime +
                "ns, contains=" + contains +
                ", addFrequency=" + addFrequency +
                "/ns, removeFrequency=" + removeFrequency +
                "/ns, collectionClearTime=" + collectionClearTime +
                "s}";
    }
}

class TableValues {
    private final String rowData;
    private final int rowLength;

    public TableValues(Object rowData, int rowLength) {
        this.rowData = rowData.toString();
        this.rowLength = rowLength;
    }

    public String getRowData() {
        return rowData;
    }
    public int getRowLength() {
        return rowLength;
    }
}

class Presenter {
    public static void displayResult(TestResult testResult, OutputType outputType, TestType testType) {
        switch (outputType) {
            case CSV -> CSVdisplay.saveTestResultToCSV(
                    testResult,
                    S33782Project01.csvPath,
                    testType
            );
            case CONSOLE -> ConsoleDisplay.displayResult(testResult, testType);
        }
    }
}

class CSVdisplay {
    public static void saveTestResultToCSV(TestResult result, String filePath, TestType testType) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(testType.name()).append('\n');

            for (int i=0; i<=1; i++) {
                if (i==0) {
                    if (result.getAddTime() != null)
                        sb.append("AddTime,");

                    if (result.getSearchTime() != null)
                        sb.append("SearchTime,");

                    if (result.getRemoveTime() != null)
                        sb.append("RemoveTime,");

                    if (result.contains() != null)
                        sb.append("Contains,");

                    if (result.getAddFrequency() != null)
                        sb.append("AddFrequency,");

                    if (result.getRemoveFrequency() != null)
                        sb.append("RemoveFrequency,");

                    if (result.getCollectionClearTime() != null)
                        sb.append("CollectionClearTime\n");

                } else {
                    if (result.getAddTime() != null) {
                        sb.append(result.getAddTime()).append(",");
                    }
                    if (result.getSearchTime() != null) {
                        sb.append(result.getSearchTime()).append(",");
                    }
                    if (result.getRemoveTime() != null) {
                        sb.append(result.getRemoveTime()).append(",");
                    }
                    if (result.contains() != null) {
                        sb.append(result.contains()).append(",");
                    }
                    if (result.getAddFrequency() != null) {
                        sb.append(result.getAddFrequency()).append(",");
                    }
                    if (result.getRemoveFrequency() != null) {
                        sb.append(result.getRemoveFrequency()).append(",");
                    }
                    if (result.getCollectionClearTime() != null) {
                        sb.append(result.getCollectionClearTime()).append("\n");
                    }
                }
            }
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ConsoleDisplay {
    public static void displayResult(TestResult testResult, TestType testType) {
        System.out.println(testType.name());

        Map<String, TableValues> rowsLength = new LinkedHashMap<>();

        if (testResult.getAddTime() != null)
            rowsLength.put(
                    " AddTime ",
                    new TableValues(
                            testResult.getAddTime(),
                            rowLength("AddTime", testResult.getAddTime())
                    )
            );

        if (testResult.getSearchTime() != null)
            rowsLength.put(
                    " SearchTime ",
                    new TableValues(
                            testResult.getSearchTime(),
                            rowLength("SearchTime", testResult.getSearchTime())
                    )
            );

        if (testResult.getRemoveTime() != null)
            rowsLength.put(
                    " RemoveTime ",
                    new TableValues(
                            testResult.getRemoveTime(),
                            rowLength("RemoveTime", testResult.getRemoveTime())
                    )
            );

        if (testResult.contains() != null)
            rowsLength.put(
                    " Contains ",
                    new TableValues(
                            testResult.contains(),
                            rowLength("Contains", testResult.contains())
                    )
            );

        if (testResult.getAddFrequency() != null)
            rowsLength.put(
                    " AddFrequency " ,
                    new TableValues(
                            testResult.getAddFrequency(),
                            rowLength("AddFrequency", testResult.getAddFrequency())
                    )
            );

        if (testResult.getRemoveFrequency() != null)
            rowsLength.put(
                    " RemoveFrequency ",
                    new TableValues(
                            testResult.getRemoveFrequency(),
                            rowLength("RemoveFrequency", testResult.getRemoveFrequency())
                    )
            );

        if (testResult.getCollectionClearTime() != null)
            rowsLength.put(
                    " CollectionClearTime ",
                    new TableValues(
                            testResult.getCollectionClearTime(),
                            rowLength("CollectionClearTime", testResult.getCollectionClearTime())
                    )
            );

        for (int row=0; row<3; row++) {
            if (row==0) {
                System.out.print('║');
                for (Map.Entry<String, TableValues> entry : rowsLength.entrySet()) {
                    System.out.print(entry.getKey());

                    int width = entry.getValue().getRowLength() - entry.getKey().length();

                    for (int i=0; i<width; i++)
                        System.out.print(' ');
                    System.out.print('║');
                }
                System.out.println();

            } else if (row==1) {
                System.out.print('═');
                for (Map.Entry<String, TableValues> entry : rowsLength.entrySet()) {
                    int width = entry.getValue().getRowLength()+1;

                    for (int i=0; i<width; i++)
                        System.out.print('═');
                }
                System.out.println();
            } else {
                System.out.print('║');
                for (Map.Entry<String, TableValues> entry : rowsLength.entrySet()) {
                    System.out.print(' ');
                    System.out.print(entry.getValue().getRowData());

                    int width = entry.getValue().getRowLength() - entry.getValue().getRowData().length() - 1;

                    for (int i=0; i<width; i++)
                        System.out.print(' ');
                    System.out.print('║');
                }
            }
        }
    }

    public static<T> Integer rowLength(String str, T t) {
        int stringLength = str.length();
        String tClassName = t.getClass().getSimpleName();
        int valueLen = 0;

        if (tClassName.equals("Long") || tClassName.equals("Double"))
            valueLen = t.toString().length();

        if (tClassName.equals("Boolean")) {
            if (t.toString().equals("true"))
                valueLen = 4;
            else
                valueLen = 5;
        }

        return Math.max(stringLength, valueLen)+2;
    }
}

// https://www.baeldung.com/java-enum-random-value
class RandomEnumGenerator<T extends Enum<T>> {

    private final Random rand = new Random(S33782Project01.seed);
    // Co ciekawe final na tablicach oznacza ze tylko referencja tablicy nie moze się zmieniać, a elementy mogą
    private final T[] val;

    public RandomEnumGenerator(Class<T> enumClassName) {
        // .getEnumConstants() - zwraca tablicę wartości w danym enumie
        val = enumClassName.getEnumConstants();
    }

    public T randEnum() {
        return val[rand.nextInt(val.length)];
    }
}

class ConvertStringToEnum {
    public static <E extends Enum<E>> E convert(Class<E> enumClass, String value) {
        return Enum.valueOf(enumClass, value.toUpperCase());
    }
}

enum CarBrand {
    HONDA("Honda"),
    FORD("Ford"),
    BMW("BMW"),
    MERCEDES("Mercedes"),
    AUDI("Audi"),
    VOLKSWAGEN("Volkswagen"),
    RENAULT("Renault"),
    PEUGEOT("Peugeot"),
    KOENIGSEGG("Koenigsegg"),
    HYUNDAI("Hyundai"),
    MAZDA("Mazda"),
    VOLVO("Volvo"),
    NISSAN("Nissan"),
    BUGATATA("Bugatata"),
    SKODA("Skoda"),
    CITROEN("Citroen"),
    FIAT("Fiat"),
    OPEL("Opel"),
    FERIRIRIRI("Feriririri"),
    TOYOTA("Toyota"),
    LEXUS("Lexus"),
    ACURA("Acura"),
    INFINITI("Infiniti"),
    DODGE("Dodge"),
    RAM("Ram"),
    JEEP("Jeep"),
    CHRYSLER("Chrysler"),
    TESLA("Tesla"),
    LINCOLN("Lincoln"),
    CADILLAC("Cadillac"),
    BUICK("Buick"),
    GMC("GMC"),
    SAAB("Saab"),
    ALFA_ROMEO("Alfa Romeo"),
    LANCIA("Lancia"),
    MASERATI("Maserati"),
    ASTON_MARTIN("Aston Martin"),
    BENTLEY("Bentley"),
    ROLLS_ROYCE("Rolls-Royce"),
    MITSUBISHI("Mitsubishi"),
    SUZUKI("Suzuki"),
    SUBARU("Subaru"),
    DAIHATSU("Daihatsu"),
    TATA("Tata"),
    SEAT("Seat"),
    GEELY("Geely"),
    BYD("BYD"),
    RIVIAN("Rivian"),
    LUCID("Lucid"),
    CHEVROLET("Chevrolet");

    private final String displayBrand;

    CarBrand(String displayBrand) {
        this.displayBrand = displayBrand;
    }

    // Dzięki temu CarBrand.HONDA.toString() zwróci "Honda"
    public String getBrand() {
        return displayBrand;
    }
}

enum CollectionSize {
    S100(100),
    S500(500),
    S1000(1000),
    S10000(10000),
    CUSTOM(-1);

    private final int size;

    CollectionSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean isCustom() {
        return this == CUSTOM;
    }
}

enum CollectionType {
    ARRAYLIST {
        public boolean hasIndexes() { return true; }
    },
    LINKEDLIST{
        public boolean hasIndexes() { return true; }
    },
    HASHSET{
        public boolean hasIndexes() { return false; }
    },
    TREESET {
        public boolean hasIndexes() { return false; }
    };

    public abstract boolean hasIndexes();
}

enum DataType {
    INTEGER, DOUBLE, PERSON, MYCOLOR, CAR, SCREEN
}

enum OutputType {
    CONSOLE, CSV
}

enum PeopleNames {
    ADAM("Adam"),
    ALEX("Alex"),
    BENJAMIN("Benjamin"),
    CHARLES("Charles"),
    DANIEL("Daniel"),
    AMY("Amy"),
    BELLE("Belle"),
    EMILY("Emily"),
    GRACE("Grace"),
    JACK("Jack"),
    OLIVER("Oliver"),
    WILLIAM("William"),
    JAMES("James"),
    GEORGE("George"),
    HARRY("Harry"),
    CHARLOTTE("Charlotte"),
    LILY("Lily"),
    SOPHIA("Sophia"),
    ISLA("Isla"),
    AVA("Ava"),
    MIA("Mia"),
    ELLA("Ella"),
    EMMA("Emma"),
    JACKSON("Jackson"),
    SAMUEL("Samuel"),
    LEVI("Levi"),
    AIDAN("Aidan"),
    LUCAS("Lucas"),
    ETHAN("Ethan"),
    JACKIE("Jackie"),
    LEO("Leo"),
    MAX("Max"),
    BEN("Ben"),
    NOAH("Noah"),
    LUCY("Lucy"),
    OLIVIA("Olivia"),
    ARIA("Aria"),
    ELIZABETH("Elizabeth"),
    SARA("Sara"),
    ZOE("Zoe"),
    MOLLY("Molly"),
    KATE("Kate"),
    RUBY("Ruby"),
    HAILEY("Hailey"),
    LAYLA("Layla"),
    SOPHIE("Sophie"),
    NATALIE("Natalie"),
    JULIA("Julia"),
    RACHEL("Rachel"),
    CLAIRE("Claire"),
    KAREN("Karen"),
    SARAH("Sarah"),
    RILEY("Riley"),
    LEAH("Leah"),
    MAYA("Maya"),
    PIPER("Piper"),
    HARPER("Harper"),
    ARDEN("Arden"),
    IRENE("Irene"),
    ISABELLA("Isabella"),
    HANNAH("Hannah"),
    ABIGAIL("Abigail"),
    CAROLINE("Caroline"),
    MARGARET("Margaret"),
    LEXI("Lexi"),
    ALICE("Alice"),
    REBECCA("Rebecca"),
    EVA("Eva"),
    MILA("Mila"),
    CAMILA("Camila"),
    DAISY("Daisy"),
    TESSA("Tessa"),
    RITA("Rita"),
    VERA("Vera"),
    ALEXA("Alexa"),
    LILLIAN("Lillian"),
    CORA("Cora"),
    STELLA("Stella"),
    JOANNA("Joanna"),
    MARIANNA("Marianna"),
    JULIET("Juliet"),
    ALMA("Alma"),
    DOROTHY("Dorothy"),
    CLEMENTINE("Clementine"),
    ANNA("Anna"),
    ELEANOR("Eleanor"),
    LENA("Lena"),
    GABRIELLA("Gabriella"),
    ARIEL("Ariel"),
    ADDISON("Addison"),
    NORA("Nora"),
    PAIGE("Paige"),
    KIMBERLY("Kimberly"),
    MAE("Mae"),
    KENDALL("Kendall"),
    GABRIELA("Gabriela"),
    LUCINDA("Lucinda"),
    TAYLOR("Taylor");

    private final String name;

    PeopleNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

enum PeopleSurnames {
    SMITH("Smith"),
    JOHNSON("Johnson"),
    WILLIAMS("Williams"),
    JONES("Jones"),
    BROWN("Brown"),
    DAVIS("Davis"),
    MILLER("Miller"),
    WILSON("Wilson"),
    MOORE("Moore"),
    TAYLOR("Taylor"),
    ANDERSON("Anderson"),
    THOMAS("Thomas"),
    JACKSON("Jackson"),
    WHITE("White"),
    HARRIS("Harris"),
    MARTIN("Martin"),
    THOMPSON("Thompson"),
    GARCIA("Garcia"),
    MARTINEZ("Martinez"),
    ROBERTS("Roberts"),
    CLARK("Clark"),
    LEWIS("Lewis"),
    WALKER("Walker"),
    YOUNG("Young"),
    ALLEN("Allen"),
    KING("King"),
    SCOTT("Scott"),
    GREEN("Green"),
    ADAMS("Adams"),
    BAKER("Baker"),
    HALL("Hall"),
    NELSON("Nelson"),
    CARTER("Carter"),
    MITCHELL("Mitchell"),
    PEREZ("Perez"),
    ROBINSON("Robinson"),
    HARRISON("Harrison"),
    MURPHY("Murphy"),
    COOPER("Cooper"),
    RICHARDSON("Richardson"),
    COLEMAN("Coleman"),
    SANCHEZ("Sanchez"),
    ROGERS("Rogers"),
    REED("Reed"),
    COOK("Cook"),
    MORRIS("Morris"),
    BELL("Bell"),
    MURRAY("Murray"),
    PETERSON("Peterson"),
    GRAY("Gray"),
    JAMES("James"),
    WASHINGTON("Washington"),
    BUTLER("Butler"),
    SIMMONS("Simmons"),
    FOSTER("Foster"),
    GONZALEZ("Gonzalez"),
    BRYANT("Bryant"),
    ALEXANDER("Alexander"),
    RUSSELL("Russell"),
    GRANT("Grant"),
    HAMILTON("Hamilton"),
    FISHER("Fisher"),
    GRIFFIN("Griffin"),
    WELLS("Wells"),
    WEBB("Webb"),
    DUNCAN("Duncan"),
    HOPKINS("Hopkins"),
    KELLY("Kelly"),
    CHAVEZ("Chavez"),
    GORDON("Gordon"),
    WALTERS("Walters"),
    MASON("Mason"),
    CUNNINGHAM("Cunningham"),
    WHEELER("Wheeler"),
    HUNTER("Hunter"),
    CURTIS("Curtis"),
    STEWART("Stewart"),
    BARNES("Barnes"),
    HENDERSON("Henderson"),
    RAMIREZ("Ramirez"),
    FIELDS("Fields"),
    GREENE("Greene"),
    ROYAL("Royal"),
    SIMS("Sims"),
    REYNOLDS("Reynolds"),
    PERRY("Perry"),
    PATTERSON("Patterson"),
    SANDERS("Sanders"),
    HUNT("Hunt"),
    GALLAGHER("Gallagher"),
    BYRD("Byrd"),
    DOUGLAS("Douglas"),
    KIM("Kim"),
    LONG("Long"),
    WEST("West"),
    PHELPS("Phelps"),
    RAY("Ray"),
    GIBSON("Gibson"),
    MENDOZA("Mendoza"),
    WANG("Wang"),
    JAMESON("Jameson");

    private final String surname;

    PeopleSurnames(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return surname;
    }

}

enum TestType {
    GET_BY_INDEX,
    ADD_REMOVE_FREQUENCY,
    GET_BY_VALUE,
    CONTAINS
}