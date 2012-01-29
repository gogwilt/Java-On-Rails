# Base controller impelementation which Application-level controllers will extend.
# @author rbuckheit

module JORController
  
  CONTENT_TYPES = {
    :html => ".html.erb",
    :json => ".json"
  }
  
  class Base
    
    # return dynamically generated content page for the given target method
    def render(target_method, content_type)
      
    end
    
  end
  
end