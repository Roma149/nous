package roma.nous

import scala.math.exp

import scala.collection.mutable.ListBuffer

class Neuron(val identifier: String = "") {

	var value: Double = 0
	var bias: Double = 0

	//var outgoing: Array[(Neuron, Double)] // list?
	val incoming: ListBuffer[Connection] = new ListBuffer[Connection]() // how to change into a list?
	val outgoing: ListBuffer[Connection] = new ListBuffer[Connection]()

	var received_amount: Int = 0
	var received_value: Double = 0

	def propagate_forward(): Unit = {
		if (outgoing.length > 0) {
			for (connection <- outgoing) {
				println("propagate forward value: " + value + " bias: " + bias)
				connection.propagate_forward(value, bias)
			}	
		}
	}

	def add_incoming(conn: Connection) = this.incoming += conn

	def add_outgoing(conn: Connection) = this.outgoing += conn

	def receive_forward(value: Double) = {

		this.received_value += value

		this.received_amount += 1

		// check if all have been received
		if (received_amount == incoming.length) {
			// recalculate this neuron's value with the sigmoid function
			this.value = 1 / (1 + exp(-value))
			// start forward propagation
			this.propagate_forward()
		}
	}

	//def propagate_backwards() = {}
	//def receive_backwards() = {}

	def reset() = {
		this.value = 0
		this.received_amount = 0
		this.received_value = 0
	}
}