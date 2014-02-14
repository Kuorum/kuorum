import kuorum.core.model.CommissionType
import kuorum.core.model.PostType
import kuorum.law.Law

fixture {
    aborto(Law){
        hashtag = "#leyAborto"
        shortName = "Ley del aborto"
        realName = "Ley de Protección de la Vida Concebido y de los Derechos de la Mujer Embarazada"
        description = """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam hendrerit condimentum eros. Curabitur eget mattis orci. Integer id aliquet purus. Suspendisse suscipit erat ut blandit aliquam. Nam venenatis aliquet tempor. Quisque leo leo, mattis nec egestas ut, lacinia eget neque. Praesent ac egestas lorem, nec blandit urna. Sed condimentum placerat nibh id dictum. Donec consequat ipsum laoreet, dignissim massa vitae, accumsan diam. Sed sed tristique neque.

Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Curabitur vitae nunc vitae dolor ultricies consequat. Suspendisse nec dignissim orci. Vivamus tempor tellus non nulla condimentum volutpat. Ut sed lorem enim. Quisque vel augue congue, facilisis turpis interdum, placerat orci. Fusce quis mattis nisi. Nullam semper magna non congue ullamcorper. Cras eleifend, lacus in imperdiet gravida, elit diam vehicula tortor, ut ultricies lorem lacus at tellus. Maecenas rhoncus magna id augue posuere, at tristique leo interdum. Praesent lacus velit, vestibulum at dolor vel, ornare auctor nulla. Maecenas tristique consectetur mi, ut mattis metus. Ut vitae magna pharetra, posuere elit quis, porta libero. Praesent luctus nulla diam, non cursus sem venenatis nec.

Vivamus non sapien eget eros sodales consectetur et ut magna. Vivamus quis risus eleifend risus tincidunt malesuada id eget elit. Pellentesque vel nisi eu dolor commodo interdum. Aenean aliquet facilisis leo, eu venenatis lorem. Duis mattis ligula sit amet nisl dapibus, ut consequat tortor laoreet. Cras a suscipit nulla. Quisque at tellus tempus, tempor orci sed, pellentesque est. Mauris a blandit mauris. Cras eget porttitor sem.
        """
        introduction="""
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam hendrerit condimentum eros. Curabitur eget mattis orci. Integer id aliquet purus. Suspendisse suscipit erat ut blandit aliquam. Nam venenatis aliquet tempor. Quisque leo leo, mattis nec egestas ut, lacinia eget neque. Praesent ac egestas lorem, nec blandit urna. Sed condimentum placerat nibh id dictum. Donec consequat ipsum laoreet, dignissim massa vitae, accumsan diam. Sed sed tristique neque.
"""
        commissions = [CommissionType.JUSTICE, CommissionType.EMPLOY_AND_HEALTH_SERVICE]
        region = spain
    }
}