def call(String label, code)
{ 
    podTemplate(label: label,
        //cloud: 'kubernetes',
        namespace: 'kubernetes-plugin',
        containers: [containerTemplate(name: 'jnlp',
            // alwaysPullImage: true,
            image: '10.69.182.169:18080/customjnlp:latest',
            args: '${computer.jnlpmac} ${computer.name}')],
        imagePullSecrets: [ 'regcred' ]) {
            code()
        }
}

