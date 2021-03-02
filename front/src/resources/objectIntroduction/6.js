export const task = `
-> class
    with field
        with modifiers: { "private" }
        with type: "boolean"
        with name: "b"
-> class
    with field
        with modifiers: { "private" }
        with type: "int"
        with name: "i"
        with init text: "1"
`;

export const input = `public class MyFirstClass {

}`;

export const description = `Poza samymi metodami, klasa może posiadać również liczne <b>pola</b>. Są one atrybutami klasy, pełniącymi rolę podobną do zmiennych, jednak są one widoczne w ramach całej klasy. Dzięki takiemu mechanizmowi możliwe jest używanie wartości zapisanych w polach bez konieczności przekazywania ich jako dodatkowe parametry. Pola danych można traktować jako miejsca, gdzie zapisywany jest aktualny stan obiektu.
Pola deklaruje się analogicznie jak zmienne lokalne (można to poprzedzić opcjonalnymi modyfkiatorami):
<java>
TYP_POLA NAZWA;
</java>`;

export const instruction = `Zdefiniuj prywatne pola "b" oraz "i", które będą mogły przchowywać kolejno wartości logiczne oraz liczby całkowite (skorzystaj z typów prostych). W ramach deklracji zainicjalizuj pole "i" wartością 1.`;

export const hasNext = true;