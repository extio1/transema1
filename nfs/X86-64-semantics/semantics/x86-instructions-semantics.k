requires "x86-configuration.k"
requires "x86-flag-checks-syntax.k"

module X86-INSTRUCTIONS-SEMANTICS
  imports X86-CONFIGURATION
  imports X86-FLAG-CHECKS-SYNTAX

// movq-r64-m64

  context execinstr(movq:Opcode HOLE:Mem, R2:R64,  .Operands) [result(MemOffset)]
  
  rule <k>
    execinstr (movq:Opcode memOffset( MemOff:MInt):MemOffset, R2:R64,  .Operands) =>
      loadFromMemory( MemOff, 64) ~>
      execinstr (movq memOffset( MemOff), R2,  .Operands)
  ...</k>
    <regstate> RSMap:Map </regstate>
          
  rule <k>
    memLoadValue(Mem64:MInt):MemLoadValue ~> execinstr (movq:Opcode memOffset( MemOff:MInt):MemOffset, R2:R64,  .Operands) =>
      .
  ...</k>
    <regstate>
      RSMap:Map => updateMap(RSMap,
convToRegKeys(R2) |-> Mem64
      )
    </regstate>

[klabel(MOVQ-R64-M64)]

// movq-r64-r64

  rule <k>
    execinstr (movq R1:R64, R2:R64,  .Operands) => .
  ...</k>
    <regstate>
RSMap:Map => updateMap(RSMap,
convToRegKeys(R2) |-> getParentValue(R1, RSMap)
)

    </regstate>
    

[klabel(MOVQ-R64-R64)]

// movq-r64-r64-semantics


// movq-r64-imm64

  rule <k>
    execinstr (movq Imm64:Imm, R2:R64,  .Operands) => .
  ...</k>
    <regstate>
RSMap:Map => updateMap(RSMap,
convToRegKeys(R2) |-> handleImmediateWithSignExtend(Imm64, 64, 64)
)

    </regstate>
    

[klabel(MOVQ-R64-IMM64)]

// movq-r64-imm64-semantics

endmodule