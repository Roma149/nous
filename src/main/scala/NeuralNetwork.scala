package roma.nous

/** A neural network with a single hidden layer
 *
 * @constructor create a new neural network by specifying its inputs, outputs and size of the hidden layer
 * @param inputs array of string with identifiers for the independent variables
 * @param outputs array of strings with identifiers for the dependent variables
 * @param hidden integer specifying the amount of neurons in the hidden layer
 */
class NeuralNetwork(val inputs: Array[String], val outputs: Array[String], val hidden: Int) {

	// raise an error if the amount of inputs, outputs or size of the hidden layer is less than 1
	require(inputs.length >= 1, "the number of inputs must be equal or greater than 1.")
	require(outputs.length >= 1, "the number of outputs must be equal or greater than 1.")
	require(hidden >= 1, "there must be at least one neuron in the hidden layer")

	// generate neurons for each layer from the supplied identifiers
	val input_layer: List[Neuron] = List.tabulate(inputs.length) (n => new Neuron(inputs(n)))
	val output_layer: List[Neuron] = List.tabulate(outputs.length) (n => new Neuron(outputs(n)))
	val hidden_layer: List[Neuron] = List.tabulate(hidden) (n => new Neuron())

	val rng = scala.util.Random // generator for random initial weights

	var completed_outputs: Int = 0 // to check whether all outputs have completed forward propagation

	// generate connections between the input and the hidden layers
	for (input_neuron <- input_layer) {
		for (hidden_neuron <- hidden_layer) {
			val connection: Connection = new Connection(input_neuron, hidden_neuron, rng.nextDouble)
			input_neuron.add_outgoing(connection)
			hidden_neuron.add_incoming(connection)
		}
		// to list?
	}

	// generate connections between the hidden and the output layers
	for (hidden_neuron <- hidden_layer) {
		for (output_neuron <- output_layer) {
			val connection: Connection = new Connection(hidden_neuron, output_neuron, rng.nextDouble)
			hidden_neuron.add_outgoing(connection)
			output_neuron.add_incoming(connection)
		}
	}

	def set_inputs(inputs: Array[Double]) = {
		// error if inputs != input_layer.length
		for (i <- 0 until input_layer.length) {
			input_layer(i).value = inputs(i)
		}
	}

	def begin_forward_propagation() = {
		for (i <- 0 until input_layer.length) {
			println("Begin forward propagation from neuron " + i + " with id " + input_layer(i).identifier)
			input_layer(i).propagate_forward()
		}
	}

	def reset_network() = {
		this.completed_outputs = 0
		for (neuron <- input_layer) neuron.reset()
		for (neuron <- hidden_layer) neuron.reset()
		for (neuron <- output_layer) neuron.reset()
	}

	def print_output() = {
		for (i <- 0 until output_layer.length) {
			println("Output from Neuron #" + i + "(" + output_layer(i).identifier + "): " + output_layer(i).value)
		}
	}
}

object NeuralNetwork {
	def main(args: Array[String]) {
		val inputs = Array("a", "b", "c")
		val outputs = Array("z")
		val nn = new NeuralNetwork(inputs, outputs, 4)

		nn.set_inputs(Array(1,2,3))
		nn.begin_forward_propagation()
		nn.print_output()
	}
}