////////////////////;
// abc grammar;
music ::= header body_element;

////////////////////;
// header;
@skip WHITESPACE{
    header ::= field_number field_title other_fields* field_key;
    field_number ::= "X:" NUMBER end_of_line;
    field_title ::= "T:" TEXT end_of_line;
    other_fields ::= field_composer | field_default_length | field_meter | field_tempo | field_voice | comment;
    field_composer ::= "C:" TEXT end_of_line;
    field_default_length ::= "L:" note_length_strict end_of_line;
    field_meter ::= "M:" meter end_of_line;
    field_tempo ::= "Q:" tempo end_of_line;
    field_voice ::= "V:" TEXT end_of_line;
    field_key ::= "K:" key end_of_line;
}

key ::= keynote "m"?;
keynote ::= base_note key_accidental?;
key_accidental ::= "#" | "b";

meter ::= "C" | "C|" | meter_fraction;
meter_fraction ::= NUMBER "/" NUMBER;

tempo ::= meter_fraction "=" NUMBER;

body_element ::= (TEXT | NEWLINE | comment)+;

////////////////////;
// multi_voice;
@skip WHITESPACE{
    multi_voice ::= part+;
    part ::= (comment NEWLINE) | ("V:" TEXT NEWLINE voice_part);

    voice_part ::= note_line+;
    note_line ::= [^V%\n\r]+ NEWLINE?;
}

////////////////////;
// voice ;
@skip WHITESPACE_ALL{
    voice ::= "|"? section+;
    section ::= repeat_phrase | phrase;

    repeat_phrase ::= "|:"? (repeat | nth_repeat);
    repeat ::=  phrase bar_notes ":|";
    nth_repeat ::= phrase first_repeat second_repeat;
        first_repeat ::= "[1" bar_element* bar_notes ":|";
        second_repeat ::= "[2" bar_element+ "]"?;

    phrase ::= bar_element+ "]"?;

    bar_element ::= bar_notes "|";
    bar_notes ::= element+;
}

element ::= note_element | tuplet_element;

// note_element;
note_element ::= note | multi_note;
// note is either a pitch or a rest;
note ::= note_or_rest note_length?;
note_or_rest ::= pitch | rest;
pitch ::= accidental? note_symbol;
note_symbol ::= base_note octave?;
octave ::= "'"+ | ","+;
note_length ::= NUMBER? ("/" NUMBER?)?;
rest ::= "z";
// "^" is sharp, "_" is flat, and "=" is neutral;
accidental ::= "^" | "^^" | "_" | "__" | "=";
// chord;
multi_note ::= "[" note+ "]";

// tuplet_element;
tuplet_element ::= tuplet_spec note_element+;
tuplet_spec ::= "(" [234];

////////////////////;
// general ;
base_note ::= "C" | "D" | "E" | "F" | "G" | "A" | "B" | "c" | "d" | "e" | "f" | "g" | "a" | "b";
note_length_strict ::= NUMBER "/" NUMBER;

////////////////////;
// aux ;
end_of_line ::= comment? NEWLINE;
comment ::= "%" TEXT?;

NUMBER ::= \d+;
TEXT ::= [^%\n\r]+;
NEWLINE ::= "\n" | "\r" "\n"?;
WHITESPACE ::= [ \t];
WHITESPACE_ALL ::= NEWLINE | WHITESPACE;
