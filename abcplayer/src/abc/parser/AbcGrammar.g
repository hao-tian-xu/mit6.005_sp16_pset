










// abc grammar
music ::= header body;

////////////////////
// Header
@skip WHITESPACE{
    header ::= field_number field_title other_fields* field_key;
    field_number ::= "X:" NUMBER end_of_line;
    field_title ::= "T:" text end_of_line;
    other_fields ::= field_composer | field_default_length | field_meter | field_tempo | field_voice | comment;
    field_composer ::= "C:" text end_of_line;
    field_default_length ::= "L:" note_length_strict end_of_line;
    field_meter ::= "M:" meter end_of_line;
    field_tempo ::= "Q:" tempo end_of_line;
    field_key ::= "K:" key end_of_line;
}

key ::= keynote mode_minor?;
keynote ::= basenote key_accidental?;
key_accidental ::= "#" | "b";
mode_minor ::= "m";

// TODO

////////////////////
// Body
body ::= line+;
line ::= element* NEWLINE | mid_tune_field | comment
element ::= note_element | tuplet_element | barline | nth_repeat | WHITESPACE

// TODO

////////////////////
// Aux
comment ::= "%" text
end_of_line ::= comment? NEWLINE

NUMBER ::= [0-9]+;
NEWLINE ::= "\n" | "\r" "\n"?
WHITESPACE ::= [ \t];
