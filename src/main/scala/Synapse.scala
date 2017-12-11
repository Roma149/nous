package roma.nous

class Synapse(val incoming: Neuron, val outgoing: Neuron, var weight: Double = 0) {

	def propagate_forward(value: Double, bias: Double): Unit = this.outgoing.receive_forward(weight * value + bias)

	def propagate_backwards(error: Double, derivative: Double): Unit = this.incoming.receive_backwards(weight * error * derivative)
}