.section .data

	.equ OFFSET_CHARGER_OUTPUT, 0
	.equ OFFSET_SCOOTER_ID, 4
	.equ OFFSET_BATTERY_CAPACITY, 8
	.equ OFFSET_CHARGE_PERCENTAGE, 12
	

	.global estimate
 
.section .text
 
estimate:
 
	# prologue
	pushl %ebp 			# save previous stack frame pointer
	movl %esp, %ebp 	# the stack frame pointer for our function
	push %esi
	
	movl 8(%ebp), %esi  	#struct sensor in %esi
	
	
					#cálculos utilizados:
					#( (100 - actualchargePercentage)/100 ) * (BatteryCapacity/chargerOutput) 
					# = ( (100 - actualchargePercentage) * BatteryCapacity )  /  (100 * chargerOutput)
					#Para ter em conta uma eficiencia de carga de 90% multiplicamos a BatteryCapacity por 1.1 = 11/10
					#introduzindo esses valores na equação anterior ficamos com: 
					# ( (100 - actualchargePercentage) * BatteryCapacity *11 )  /  (100 *10 * chargerOutput)
					# Para obtermos o resultado em minutos multiplicamos o numerador por 60
					# ( (100 - actualchargePercentage) * BatteryCapacity *11 *60 )  /  (1000 * chargerOutput)
					# Simplificando obtemos:
					#( (100 - actualchargePercentage) * BatteryCapacity * 66 )  / ( 100 * chargerOutput )
					####To get highter precision during division, since we "can't" use floats, we multiplied the numerator by 100 (to be reverted in main.c)###
					#Final formula:
					#( (100 - actualchargePercentage) * BatteryCapacity * 66 )  / chargerOutput
					
	
	movl $100, %eax
	subl OFFSET_CHARGE_PERCENTAGE(%esi), %eax		#(100 - actualchargePercentage) 		--> stored in %eax %eax
	
	imul  OFFSET_BATTERY_CAPACITY(%esi),%eax		# (100 - actualchargePercentage) * BatteryCapacity 			--> stored in %eax
	imul $66, %eax									# (100 - actualchargePercentage) * BatteryCapacity * 66) 	--> stored in %eax
	cdq												# "extend" %eax to  edx:eax in order to use idiv instruction safely
	
	movl OFFSET_CHARGER_OUTPUT(%esi), %ecx			#chargerOutput in %ecx

	idivl %ecx										#( (100 - actualchargePercentage) * BatteryCapacity * 114 )  /  chargerOutput
													# quotient in %eax will be returned
													# remainder in %edx will be ignored
	

	popl %esi
	movl %ebp, %esp 		# restore the previous stack pointer ("clear" the stack)
	popl %ebp 				# restore the previous stack frame pointer
	ret
