#ifndef VMINSTR_HPP
#define VMINSTR_HPP

#include <string>
#include <vector>

class VMInstr{

public:
	std::string cmd;
	std::string arg1;
	std::string arg2;
	static std::vector<VMInstr> make_vm_instrs(std::vector<std::string> vmcodes);
	int is(std::string instr);
};

#endif

/*
 
cmd can be any of:
	push,pop,return,subroutine,add,sub,...
  
*/
