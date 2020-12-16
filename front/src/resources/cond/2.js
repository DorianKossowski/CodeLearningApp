export const task = `
-> statement
    with text: "boolean greater = a > 2"
    log info: "Definicja zmiennej greater"
-> statement
    with text: "boolean lesser = a < 4"
    log info: "Definicja zmiennej lesser"
-> statement
    with text: "System.out.println(a)"
    with if: "greater && lesser"
    log info: "Wywołanie metody println wewnątrz if"
`;

export const input = `public class MyClass {
    public static void main(String[] args){

        boolean greater = a > 2;
        boolean lesser = a < 4;

    }
}`;

export const description = `Intrukcja <b>if</b> działa tak jak wskazuje na to jej nazwa. Jeżeli podany w nawiasach okrągłych warunek jest spełniony wtedy wykonywana jest logika znajdująca się w jej ciele. W innym przypadku jest ona pomijana.
<java>
if(warunek) {
	ciało
}
</java>
Warto dodać, że nawiasy klamrowe określające ciało instrukcji <b>if</b> są konieczne tylko w przypadku, gdy znajduje się tam więcej niż jedna instrukcja.`;

export const instruction = `Napisz instrukcję warunkową if, która wypisze na ekran przy użyciu metody println zmienną a, tylko wtedy gdy spełni ona zarówno warunek greater jak i lesser. Jako warunek użyj "greater && lesser".`;

export const hasNext = true;