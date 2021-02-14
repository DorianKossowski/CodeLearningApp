export const task = `
-> method
    with modifiers: { "public" }
    with result: "void"
    with name: "methodA"
    with args:
-> method
    with modifiers:
    with result: "void"
    with name: "methodB"
    with args: { "String", - }
`;

export const input = `public class MyFirstClass {

}`;

export const description = `<b>Metoda</b> jest to składowa, której zadaniem jest działanie na rzecz określonych elementów danej klasy. Metody pozwalają zdefiniować określone funkcjonalności poprzez dokonywanie w nich odpowiednich operacji czy obliczeń.
Każda klasa może mieć wiele metod, a każda z nich musi składać się opcjonlanych modyfikatorów, zwracanego typu, unikalnej (nie zawsze) nazwy oraz opcjonalnych parametrów, które są do niej przekazywane i mogą być używne tylko w ramach jej ciała. Podstawowy szablon metody wygląda następująco:
<java>
ZWRACANY_TYP NAZWA(OPCJONALNE_PARAM) {
	// kod
}
</java>
Poznane dotychczas konstruktory można określić mianem specjalnych metod. Również dokonują operacji na elementach danej klasy, jednak kluczową różnicą jest to, iż wywoływane są tylko podczas tworzenia obiektu, w przeciwieństwie do metod, które w większości są wywoływane już na samych instancjach danej klasy. Jednak o tym czym są instancje i jak wywoływać na nich metody dowiesz się w następnym rozdziale.`;

export const instruction = `Zdefiniuj dwie metody o zwracanym typie void, który określa, że metoda nie powinna zwracać żadnej wartości. Niech pierwsza "methodA" będzie publiczna i bezparametrowa, natomiast "methodB" będzie przyjmować parametr typu String i nie będzie posiadać żadnych dodatkowych modyfikatorów.`;

export const hasNext = true;