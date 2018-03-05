# Annotation Schema BNF definition

BNF definition of the syntax of AlvisAE JSON Annotation schema

*Note* for readability purposes, the character « ` » was used instead of « \" » to represent the JSON standard double-quote delimiter (« " ») 


## formatted BNF definition 

(generated from [raw BNF definition](#raw-BNF-definition) using ["BNF_CHK"](http://www.icosaedro.it/bnf_chk/bnf_chk-on-line.html) and ["HTML to wiki"](http://www.keyframesandcode.com/resources/php/redmine/documentor/))


1. upper =  *"A".."Z"* ;

2. lower =  *"a".."z"* ;

3. alpha =  _upper_1 | _lower_2 ;

4. digit =  *"0".."9"* ;

5. hexdigit =  _digit_4 | *"A".."F"* | *"a".."f"* ;

6. identifier =  _alpha_3 { _alpha_3 | _digit_4 } ;

7. intLitteral =  _digit_4 { _digit_4 } ;

8. boolLitteral =  *"true"* | *"false"* ;

9. colorLitteral =  *"`"* *"#"* _hexdigit_5 { _hexdigit_5 } *"`"* ;

10. stringLitteral =  *"`"* _identifier_6 *"`"* ;

11. stringLitteralList =  *"["* _stringLitteral_10 { *","* _stringLitteral_10 } *"]"* ;

12. safe =  *"$"* | *"-"* | *"_"* | *"@"* | *"."* | *"&amp;"* | *"+"* | *"-"* ;

13. extra =  *"!"* | *"*"* | *"\""* | *"'"* | *"("* | *")"* | *","* ;

14. escape =  *"%"* _hexdigit_5 _hexdigit_5 ;

15. special =  *"/"* | *":"* ;

16. mixalpha =  _alpha_3 | _digit_4 | _safe_12 | _extra_13 | _escape_14 | _special_15 ;

17. mixalphas =  _mixalpha_16 [ _mixalphas_17 ] ;

18. urlLitteral =  _mixalphas_17 ;

19. typeName =  _stringLitteral_10 ;

20. argumentName =  _stringLitteral_10 ;

21. propertyName =  _stringLitteral_10 ;

22. textKindDeclr =  *"`kind`"* *":"* *"O"* ;

23. relationKindDeclr =  *"`kind`"* *":"* *"1"* ;

24. groupKindDeclr =  *"`kind`"* *":"* *"2"* ;

25. typeNameDeclr =  *"`type"* *":"* _typeName_19 ;

26. colorDeclr =  *"`color"* *":"* _colorLitteral_9 ;

27. propValClosedDomainTypeDefinition =  *"{"* *"`valTypeName`"* *":"* *"`ClosedDomain`"* *","* *"`domain`"* *":"* _stringLitteralList_11 *","* *"`defaultVal`"* *":"* _stringLitteral_10 *"}"* ;

28. propValTydiTermRefTypeDefinition =  *"{"* *"`valTypeName`"* *":"* *"`TyDI_termRef`"* *","* *"`TyDIRefBaseURL`"* *":"* _urlLitteral_18 *"}"* ;

29. propValTydiSemClassRefTypeDefinition =  *"{"* *"`valTypeName`"* *":"* *"`TyDI_semClassRef`"* *","* *"`TyDIRefBaseURL`"* *":"* _urlLitteral_18 *"}"* ;

30. anyPropValueTypeDefinition =  *"null"* | _propValClosedDomainTypeDefinition_27 | _propValTydiTermRefTypeDefinition_28 | _propValTydiSemClassRefTypeDefinition_29 ;

31. propValueTypeDeclr =  *"`valType`"* *":"* _anyPropValueTypeDefinition_30 ;

32. propertyDefinition =  _propertyName_21 *":"* *"{"* *"`key`"* *":"* _propertyName_21 *","* *"`mandatory`"* *":"* _boolLitteral_8 *","* *"`minVal`"* *":"* _intLitteral_7 *","* *"`maxVal`"* *":"* _intLitteral_7 *","* *"`valType`"* *":"* _propValueTypeDeclr_31 *"}"* ;

33. propertiesDefinition =  *"{"* _propertyDefinition_32 { *","* _propertyDefinition_32 } *"}"* ;

34. typeCommonDefinition =  _typeNameDeclr_25 *","* _colorDeclr_26 *","* _propertiesDefinition_33 ;

35. boundingReference =  *"null"* ;

36. textBindingDefinition =  *"`txtBindingDef`"* *":"* *"{"* *"`minFrag`"* *":"* _intLitteral_7 *","* *"`maxFrag`"* *":"* _intLitteral_7 *","* *"`boundRef`"* *":"* _boundingReference_35 *","* *"`crossingAllowed`"* *":"* _boolLitteral_8 *"}"* ;

37. textTypeDefinition =  *"{"* _textKindDeclr_22 *","* _typeCommonDefinition_34 *","* _textBindingDefinition_36 *"}"* ;

38. typeNameList =  *"["* _typeName_19 { *","* _typeName_19 } *"]"* ;

39. argumentDeclr =  *"{"* _argumentName_20 *":"* _typeNameList_38 *"}"* ;

40. argumentDeclrList =  *"["* _argumentDeclr_39 { *","* _argumentDeclr_39 } *"]"* ;

41. relationDefinition =  *"`relationDef`"* *":"* _argumentDeclrList_40 ;

42. relationTypeDefinition =  *"{"* _relationKindDeclr_23 *","* _typeCommonDefinition_34 *","* _relationDefinition_41 *"}"* ;

43. groupDefinition =  *"`groupDef`"* *":"* *"{"* *"`minComp`"* *":"* _intLitteral_7 *","* *"`maxComp`"* *":"* _intLitteral_7 *","* *"`compType`"* *":"* _typeNameList_38 *","* *"`homogeneous`"* *":"* _boolLitteral_8 *"}"* ;

44. groupTypeDefinition =  *"{"* _groupKindDeclr_24 *","* _typeCommonDefinition_34 *","* _groupDefinition_43 *"}"* ;

45. anyAnnotationTypeDefinition =  _textTypeDefinition_37 | _relationTypeDefinition_42 | _groupTypeDefinition_44 ;

46. annotationTypeDeclr =  _typeName_19 *":"* _anyAnnotationTypeDefinition_45 ;

47. annotationTypeDeclrList =  *"{"* _annotationTypeDeclr_46 { *","* _annotationTypeDeclr_46 } *"}"* ;

48. schemaDefinition =  *"`schema`"* *":"* _annotationTypeDeclrList_47 ;



## raw BNF definition 

<pre>

upper = "A" .. "Z" ;
lower = "a" .. "z" ;
alpha = upper | lower ;
digit =  "0" .. "9" ;
hexdigit = digit | "A" .. "F" |  "a" .. "f" ;

#JSON is more permissive, but this simple definition should be enough
identifier = alpha { alpha | digit } ;
    
intLitteral = digit { digit } ;
boolLitteral = "true" | "false" ;

# actually, CSS color name are also supported 
colorLitteral = "`" "#" hexdigit { hexdigit } "`" ;
   
        
stringLitteral = "`" identifier "`" ;
stringLitteralList = "[" stringLitteral  {"," stringLitteral } "]" ;

# of course this is a simplified and very permissive definition for url
safe =  "$" | "-" | "_" | "@" | "." | "&" | "+" | "-" ;
extra = "!" | "*" | "\"" | "'" | "(" | ")" | "," ;
escape =  "%" hexdigit hexdigit ;
special = "/" | ":" ;
mixalpha = alpha | digit | safe | extra | escape | special ;
mixalphas = mixalpha [ mixalphas ] ;
urlLitteral = mixalphas ;

#it is better to have distinct names for typeName, argumentName and propertyName
typeName = stringLitteral ;
argumentName = stringLitteral ;
propertyName = stringLitteral ;


textKindDeclr = "`kind`" ":" "O" ;
relationKindDeclr = "`kind`" ":" "1" ;
groupKindDeclr = "`kind`" ":" "2" ;

typeNameDeclr = "`type" ":" typeName ;
colorDeclr = "`color" ":" colorLitteral ;

#Type of property whose values are restricted to the one specified on the `domain`
propValClosedDomainTypeDefinition = "{"
"`valTypeName`" ":" "`ClosedDomain`" "," 
"`domain`" ":" stringLitteralList "," 
"`defaultVal`" ":" stringLitteral  "}" ;


#Type of property whose values are references to a Term from TyDI
propValTydiTermRefTypeDefinition = "{"
"`valTypeName`" ":" "`TyDI_termRef`" "," 
"`TyDIRefBaseURL`" ":" urlLitteral "}" ;

#Type of property whose values are references to a Semantic Class from TyDI
propValTydiSemClassRefTypeDefinition = "{"
"`valTypeName`" ":" "`TyDI_semClassRef`" "," 
"`TyDIRefBaseURL`" ":" urlLitteral "}" ;

anyPropValueTypeDefinition = "null"
    | propValClosedDomainTypeDefinition 
    | propValTydiTermRefTypeDefinition 
    | propValTydiSemClassRefTypeDefinition ;
                             
propValueTypeDeclr = "`valType`" ":" anyPropValueTypeDefinition ;
                    
propertyDefinition = propertyName ":" "{"
"`key`" ":" propertyName ","
"`mandatory`" ":" boolLitteral ","
"`minVal`" ":" intLitteral ","
"`maxVal`" ":" intLitteral ","
"`valType`" ":" propValueTypeDeclr "}" ;
                    
                    
propertiesDefinition = "{" propertyDefinition {"," propertyDefinition } "}" ;
    
typeCommonDefinition = typeNameDeclr "," 
colorDeclr ","
propertiesDefinition ;

#not used yet
boundingReference = "null" ;
    
textBindingDefinition =  "`txtBindingDef`" ":" "{"
"`minFrag`" ":" intLitteral ","
"`maxFrag`" ":" intLitteral ","
"`boundRef`" ":" boundingReference ","
"`crossingAllowed`" ":" boolLitteral
"}" ;


textTypeDefinition = "{"  textKindDeclr ","
typeCommonDefinition ","
textBindingDefinition "}" ;
                          

typeNameList = "[" typeName  {"," typeName } "]" ;
argumentDeclr = "{" argumentName ":" typeNameList "}" ;
argumentDeclrList = "[" argumentDeclr {"," argumentDeclr } "]" ;
        
relationDefinition = "`relationDef`" ":" argumentDeclrList ;
    
relationTypeDefinition = "{"  relationKindDeclr ","
typeCommonDefinition ","
relationDefinition "}" ;

groupDefinition = "`groupDef`" ":" "{"
"`minComp`" ":" intLitteral ","
"`maxComp`" ":" intLitteral ","
"`compType`" ":" typeNameList ","
"`homogeneous`" ":" boolLitteral "}" ;
                          
groupTypeDefinition = "{"  groupKindDeclr ","
typeCommonDefinition ","
groupDefinition "}" ;


anyAnnotationTypeDefinition = textTypeDefinition | relationTypeDefinition | groupTypeDefinition ;
annotationTypeDeclr = typeName ":" anyAnnotationTypeDefinition ;
annotationTypeDeclrList = "{" annotationTypeDeclr  {"," annotationTypeDeclr } "}" ;

schemaDefinition = "`schema`" ":" annotationTypeDeclrList ;

</pre>

