export const task = `
-> method
    with modifiers: { "public", "static" }
    with result: "void"
    with name: "main"
    with args: { "String[]", - }
-> statement
    in method: "main"
    with text: "System.out.println(message);"
    with resolved: "System.out.println(\\"Hello World\\");"
    error message: "Wywołanie metody println z użyciem zmiennej"
`;

export const input = `public class Hello {
    public static void main(String[] args){
        String message = "";
    }
}`;

export const description = `W metodzie main możliwe jest korzystanie z dostępnych w języku konstrukcji. Mogą to być m.in. deklaracje zmiennych czy wywoływania innych metod.
Przykładem wywołania metody jest instrukcja System.out.println("Hello World"). Odnosi się ona do metody println znajdującej się w klasie java.io.PrintStream. Przedrostek System.out określa w tym przypadku obiekt klasy java.io.PrintStream będący polem statycznym klasy System. Dokładne omówienie czym są m.in. klasy oraz pola znajdzie się w następny rozdziałach.
W celu wypisania ciągu znaków, konieczne jest przekazanie do wyżej wymienionej metody parametru. Może to być literał znakowy ale również zdefiniowana wcześniej zmienna.`;

export const instruction = `Przy użyciu metody println wypisz ciąg znaków "Hello World". Wykorzystaj do tego celu zmienną message.`;

export const hasNext = false;