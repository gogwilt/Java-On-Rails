# Routing mechanism for the Controller Dispatcher.
# @author rbuckheit

module JORController
  
  module Routing 
    
    # general exception type for routing errors
    class RoutingError < StandardError; end
    
    CONTROLLER_SUFFIX = "controller"
    
    class Routes
      $routes = Hash.new {|h,k| h[k] = nil}
      
      def self.draw(opts = {})
        $routes = opts
      end

      def self.dump
        $routes.each{|k,v| puts "#{k} => #{v}"}
      end
      
      def self.get(route)
        return $routes[route]
      end
      
      # processes a route request by loading the controller and calling its render method.
      # TODO: handle different content-types, params, etc
      def self.process(route_request)
        route_result = $routes[route_request]
        
        if(route_result)
          controller_s, method = route_result.split("#")
          class_name = [controller_s, CONTROLLER_SUFFIX].map{|s| s.capitalize}.join("")
          
          # attempt to load controller class
          begin
            controller = Kernel.const_get(class_name)
            puts "Kernel load: #{class_name} (#{controller.class})"
          rescue NameError
            raise RoutingError, "class #{class_name} not found"
          end
          
          # check controller method existence
          if (!controller.methods.include?(method))
            raise RoutingError, "method #{method} not found" 
          end
          
          # TODO: call "render" on the controller
          # controller.render(method, content_type)
          
          return true
        end
        return false
      end

    end
  end
end