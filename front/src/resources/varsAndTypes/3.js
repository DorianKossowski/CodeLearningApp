export const task = `
-> statement
    with resolved: "byte b = 8"
    log info: "Definicja zmiennej b typu byte"
-> statement
    with resolved: "short s = 16"
    log info: "Definicja zmiennej s typu short"
-> statement
    with resolved: "int i = 32"
    log info: "Definicja zmiennej i typu int"
-> statement
    with text: "long l = 64L"
    with resolved: "long l = 64"
    log info: "Definicja zmiennej l typu long"
-> statement
    with resolved: "double d = 3.14"
    log info: "Definicja zmiennej d typu double"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

    }
}`;

export const description = `W programach niezbędne jest również używanie liczb. Java udostępnia następujące typy proste do przechowywania liczb całkowitych:
- <b>byte</b>: 8-bitowa liczba (-128 do 127)
- <b>short</b>: 16-bitowa liczba (-32768 do 32767)
- <b>int</b>: 32-bitowa liczba (-2^31 do 2^31-1)
- <b>long</b>: 64-bitowa liczba zakończona literą L (-2^63 do 2^63-1)
Aby zapisać do zmiennej liczbę zmiennoprzecinkową należy użyć jednego z typów:
- <b>float</b>: 32-bitowa liczba zakończona literą F
- <b>double</b>: 64-bitowa liczba`;

export const instruction = `Zdefiniuj kolejno zmienne b, s, i oraz l, których typy będą typami opisującymi liczby całkowite w podanej wyżej kolejności, a wartościami odpowiadające im liczby bitów, na których może być zapisana liczba tegoż typu.
Zdefiniuj zmienną d typu double, do której przypisana będzie wartość liczby pi do dwóch miejsc po przecinku.`;

export const hasNext = true;