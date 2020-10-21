export const task = `
-> statement
    with resolved: "String s = \\"definicja\\""
    log info: "Definicja zmiennej s typu String"
-> statement
    with resolved: "s = \\"przypisanie\\""
    log info: "Przypisanie do zmiennej s"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

    }
}`;

export const description = `Poza podstawową definicją zmiennej, gdzie zostaje jej nadana wartość, istnieje również możliwość ponownego przypisania do niej innej wartości. Można do tego użyć nowego literału lub też innej wcześniej zdefiniowanej zmiennej. Należy mieć na uwadze, że typ nowej wartości musi się zgadzać z typem zmiennej. Operacja przypisania ma nasępujący schemat: NAZWA_ZMIENNEJ = NOWA_WARTOŚĆ.
Mechanizm ponownego przypisywania pozwala na używanie różnych wartości zależnych od kontekstu, bez konieczności tworzenia wielu zmiennych.`;

export const instruction = `Zdefiniuj zmienną s, która będzie przechowywać napis "definicja", a następnie przypisz do niej nowy łańcuch znaków "przypisanie".`;

export const hasNext = false;