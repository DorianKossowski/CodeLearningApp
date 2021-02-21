export const task = `
-> class
    with field
        with modifiers: { "static" }
        with type: "String"
        with name: "strField"
-> method
    with modifiers: { "static" }
    with result: "void"
    with name: "setUp"
    with args:
-> method
    with modifiers: { "static" }
    with result: "String"
    with name: "createSomeString"
    with args:
-> method
    with modifiers: { "static" }
    with result: "int"
    with name: "doubleValue"
    with args: { "int", "val" }
`;

export const input = `public class MyClass {
    static String strField;

    public static void main(String[] args){
        setUp();
        System.out.println(strField);
        System.out.println(doubleValue(2));
    }

    static void setUp() {
        System.out.println("SetUp - START");
        strField = createSomeString();
        System.out.println("SetUp - DONE");
    }

    static String createSomeString() {
        System.out.println("Creating string");
        return "MyStaticName";
    }

    static int doubleValue(int val) {
        return 2 * val;
    }
}`;

export const description = `W poprzednim rozdziale dowiedziałeś się czym są metody, w tym również metody statyczne. Bazując na tej wiedzy, dowiesz się teraz jak wydzielać poszczególne elementy programu do odzielnych metod. Jest to kluczowy element tworzenia oprogramowania, ponieważ pozwala na dzielenie logiki aplikacji na mniejsze części, dzięki czemu kod staje się czytelniejszy, a rónież pozwala na reużywalność mechanizmów bez konieczności ich ponownego implementowania.
W tym rozdziale wszystkie mechanizmy działania przedstawione zostaną na metodach statycznych, jednak stosuję się je analogicznie przy metodach obiektowych.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on mechanizmy, które poznasz w tym rozdziale.`;

export const hasNext = true;