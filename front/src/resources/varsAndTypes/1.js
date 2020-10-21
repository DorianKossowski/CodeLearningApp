export const task = `
-> statement
    with text: "String s = \\"My String\\""
    error message: "Brak deklaracji zmiennej s typu String"
-> statement
    with text: "char c = 'C'"
    error message: "Brak deklaracji zmiennej c typu char"
-> statement
    with text: "int i = 5"
    error message: "Brak deklaracji zmiennej i typu int"
-> statement
    with text: "double d = .5d"
    error message: "Brak deklaracji zmiennej d typu double"
-> statement
    with text: "int i2 = i"
    with resolved: "int i2 = 5"
    error message: "Brak deklaracji zmiennej i2 typu int"
-> statement
    with text: "boolean b = true"
    error message: "Brak deklaracji zmiennej b typu boolean"
-> statement
    with text: "int result = (i+i2) * 2"
    with resolved: "int result = 20"
    error message: "Brak deklaracji zmiennej result typu int"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        String s = "My String";
        char c = 'C';

        int i = 5;
        double d = .5d;
        int i2 = i;
        int result = (i+i2) * 2;

        boolean b = true;
        b = false;
    }
}`;

export const description = `Zmienne są specjalnym elementem języka, które pozwalają przechowywać warotści. Dzięki takiemu mechanizmowi, możliwe jest odwoływanie się do jednej wartości z wielu miejsc w kodzie bez konieczności jej ciągłego zapisywania. Główną zaletą zmiennych jest to, że pozwalają one zmienić wielokrotnie używaną wartość, poprzez modyfikację w jednym miejscu.
Proces tworzenia zmiennej składa się z dwóch etapów: deklaracji, gdzie określany jest typ i nazwa oraz inicjalizacji, gdzie nadawana jest konkretna wartość. Często również sprowadza się te dwa mechanizmy do jednej definicji zmiennej, której schemat jest następujący: TYP NAZWA_ZMIENNEJ = WARTOŚĆ.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje, które poznasz w tym rozdziale.`;

export const hasNext = true;