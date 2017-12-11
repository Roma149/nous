package roma.nous

import scala.math.exp

import scala.collection.mutable.ListBuffer

class Neuron(val identifier: String = "") {

	var value: Double = 0 // rename to 'output'
	var bias: Double = 0

	//var outgoing: Array[(Neuron, Double)] // list?
	val incoming: ListBuffer[Synapse] = new ListBuffer[Synapse]() // how to change into a list?
	val outgoing: ListBuffer[Synapse] = new ListBuffer[Synapse]()

	// TEST: try having weights for each outgoing in the neuron itself
	//var incoming: List[Neuron] = new List[Neuron]()
	//var outgoing: Map[Neuron] = new Map[Neuron]() // list of (neuron, weight) tuples

	var received_amount: Int = 0
	var received_value: Double = 0

	var expected: Double = 0 // only used for output neurons, maybe just use as argument

	var network: Option[NeuralNetwork] = None

	def propagate_forward(): Unit = {
		if (outgoing.length > 0) { // not an output neuron, so continue propagating forward
			for (synapse <- outgoing) {
				//println("propagate forward value: " + value + " bias: " + bias)
				synapse.propagate_forward(value, bias)
			}	
		} else { // it's an output neuron, so notify the network
			network.notify_completed()
		}
	}

	def add_incoming(synapse: Synapse) = this.incoming += synapse

	def add_outgoing(synapse: Synapse) = this.outgoing += synapse

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

	def propagate_backwards() = {
		for (synapse <- incoming) {
			synapse.propagate_backwards(error())
		}
	}

	def receive_backwards(error: Double) = {

	}

	def error(): Double = (expected - value) * derivative(value)

	def derivative(): Double = value * (1 - value)

	def reset() = {
		this.value = 0
		this.received_amount = 0
		this.received_value = 0
	}

	def is_output(): Boolean = outgoing.length == 0 && incoming.length > 0
	def is_input(): Boolean = incoming.length == 0 && outgoing.length > 0
	def is_hidden(): Boolean = incoming.length > 0 && outgoing.length > 0
}